package com.example.vcloset.logic.service.outfit;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.category.CategoryEnum;
import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.outfit.Outfit;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OutfitService {
    public static final String SUPERIOR = "SUPERIOR";
    public static final String INFERIOR = "INFERIOR";
    public static final String ABRIGO = "ABRIGO";
    public static final String CUERPO_COMPLETO = "CUERPO_COMPLETO";
    public static final String ACCESORIO = "ACCESORIO";
    public static final String TYPE = "type";
    public static final String CALZADO = "CALZADO";
    public static final String COLOR = "color";
    public static final String IMAGE_URL = "image_url";
    public static final String ID = "id";
    public static final String SUB_TYPE = "sub_type";
    public static final String VESTIDOS = "VESTIDOS";
    public static final String NAME_CATEGORY = "nameCategory";
    public static final String SEASON = "season";
    public static final String ID_CATEGORY = "idCategory";
    private List<Outfit> outfit = new ArrayList<>();
    private boolean isDress = false;
    private String selectionType;
    private double temp;
    private boolean isFormal = false;
    Map<String, List<Map<String, String>>> categories = new HashMap<>();
    public static List<Map<String, String>> temporalTop = new ArrayList<>();
    public static List<Map<String, String>> temporalUnder = new ArrayList<>();
    public static List<Map<String, String>> temporalCoat = new ArrayList<>();
    public static List<Map<String, String>> temporalFootwear = new ArrayList<>();
    public static List<Map<String, String>> temporalFullBody = new ArrayList<>();
    public static List<Map<String, String>> temporalAccesory = new ArrayList<>();
    public static List<Outfit> weeklyOutfit = new ArrayList<>();


    public List<Outfit> getTypeList(List<Map<String, Object>> temporal, String _selectionType, float _temp) {
        Random random = dataSettings(temporal, _selectionType);
        getOutfitRandom(random);
        return outfit;
    }

    public List<Outfit> getWeeklyOutfits(List<Map<String, Object>> temporal, String _selectionType, float _temp){
        Random random = dataSettings(temporal, _selectionType);
        temp = _temp;
        for (int i = 0; i <= 6 ; i++) {
            getOutfitRandom(random);
        }
        return weeklyOutfit;

    }

    private Random dataSettings(List<Map<String, Object>> temporal, String _selectionType) {
        selectionType = _selectionType;
        outfit = new ArrayList<>();
        weeklyOutfit = new ArrayList<>();
        // Crear un mapa para agrupar las listas por tipo

        categories.put(SUPERIOR, new ArrayList<>());
        categories.put(INFERIOR, new ArrayList<>());
        categories.put(ABRIGO, new ArrayList<>());
        categories.put(CALZADO, new ArrayList<>());
        categories.put(CUERPO_COMPLETO, new ArrayList<>());
        categories.put(ACCESORIO, new ArrayList<>());

        // Recorrer la lista del Store procedure para desglosarlo en las categorías
        for (Map<String, Object> row : temporal) {
            String type = (String) row.get(TYPE);
            if (categories.containsKey(type)) {
                categories.get(type).add(addRow(row, type));
            }

        }
        temporalLists();

        return new Random();
    }

    private void temporalLists() {
        temporalTop=new ArrayList<>(categories.get(SUPERIOR));
        temporalUnder=new ArrayList<>(categories.get(INFERIOR));
        temporalCoat=new ArrayList<>(categories.get(ABRIGO));
        temporalFootwear=new ArrayList<>(categories.get(CALZADO));
        temporalFullBody=new ArrayList<>(categories.get(CUERPO_COMPLETO));
        temporalAccesory=new ArrayList<>(categories.get(ACCESORIO));
    }

    private void getOutfitRandom(Random random) {
        if (!categories.get(SUPERIOR).isEmpty() && !categories.get(CUERPO_COMPLETO).isEmpty()) {
            boolean randomBool = random.nextBoolean();

            // Si es 'true', se selecciona de 'SUPERIOR'
            if (randomBool) {
                // Selección de SUPERIOR, INFERIOR, CALZADOS, ABRIGOS
                selectRandomFromList(categories.get(SUPERIOR),SUPERIOR, false);
                Object subTypeObject = categories.get(SUB_TYPE);

                selectRandomFromList(categories.get(INFERIOR), INFERIOR, false);
                selectRandomFromList(categories.get(CALZADO), CALZADO, false);
                selectRandomFromList(categories.get(ABRIGO), ABRIGO, true);
            } else {
                // Si es 'false', se selecciona de 'CUERPO_COMPLETO'
                selectRandomFromList(categories.get(CUERPO_COMPLETO), CUERPO_COMPLETO, false);
                if(!isDress) {
                    selectRandomFromList(categories.get(SUPERIOR), SUPERIOR, false);
                }
                selectRandomFromList(categories.get(CALZADO), CALZADO, false);
                selectRandomFromList(categories.get(ACCESORIO), ACCESORIO, true);
            }
        } else {
            // Si 'SUPERIOR' no tiene elementos selecciona de cuerpo completo
            if(!categories.get(SUPERIOR).isEmpty()){
                selectRandomFromList(categories.get(SUPERIOR), SUPERIOR, false);

                // Lista de categorías a verificar
                List<String> categoriesToCheck = List.of(
                        INFERIOR, CALZADO, ABRIGO, ACCESORIO
                );
                // Iterar sobre las categorías adicionales
                for (int i = 0; i < categoriesToCheck.size(); i++) {
                    String category = categoriesToCheck.get(i);
                    // Verificar si la categoría no está vacía
                    if (!categories.get(category).isEmpty()) {
                        boolean isLastCategory = (i == categoriesToCheck.size() - 1);
                        selectRandomFromList(categories.get(category), category, isLastCategory );
                    }
                }


            }else if (!categories.get(CUERPO_COMPLETO).isEmpty()){
                selectRandomFromList(categories.get(CUERPO_COMPLETO), CUERPO_COMPLETO, false);
                if(!isDress) {
                    selectRandomFromList(categories.get(SUPERIOR), SUPERIOR, false);
                }
                selectRandomFromList(categories.get(CALZADO), CALZADO, false);
                selectRandomFromList(categories.get(ACCESORIO), ACCESORIO, true);
            }
        }

    }


    //Metodo para seleccionar aleatoriamente un elemento de la lista
    private void selectRandomFromList(List<Map<String, String>> list, String type, boolean flag) {
        // Si la selección no es aleatoria, y la lista está vacía verificamos si necesita reinicarla
       if (selectionType.equals("weekly") && list.isEmpty()) {
           processSpecificSelection(type);
           return;
        }else if (list.isEmpty()){
           return;
        }


        Map<String, String> selectedItem = new HashMap<>();
        List<Map<String, String>> filteredList = new ArrayList<>();
        List<Map<String, String>> formalList = new ArrayList<>();

        // Si la lista tiene más de un elemento y se cumple la condición de temperatura
        if (list.size() > 1 && temp <= 24) {
            // Filtramos las listas por invierno y tipo de ropa
            filterListsBySeasonAndCategory(list, type, formalList, filteredList);

            // Intentamos seleccionar un item formal si existe alguna opción en esa lista
            if (!formalList.isEmpty()) {
                selectedItem = getRandomItem(formalList);
            } else if(!filteredList.isEmpty()){
                // Si no hay formal, seleccionamos de la lista no formal
                selectedItem = getRandomItem(filteredList);
            }
        }

        // Si aún no se ha seleccionado un item, seleccionamos uno aleatorio de la lista original
        if (selectedItem.isEmpty()) {
            selectedItem = getRandomItem(list);
        }
        if(selectedItem.get(NAME_CATEGORY).equals("FORMAL") && (type.equals(SUPERIOR) || type.equals(CUERPO_COMPLETO))){
            isFormal=true;
        }else{
            isFormal=false;
        }
        // Actualizamos las categorías y agregamos la ropa
        categories.get(type).remove(selectedItem);
        isDress = selectedItem.get(SUB_TYPE).contains(VESTIDOS);
        addClothing(selectedItem, flag);


    }

    private void filterListsBySeasonAndCategory(List<Map<String, String>> list, String type,
                                                List<Map<String, String>> formalList,
                                                List<Map<String, String>> filteredList) {
        for (Map<String, String> row : list) {
            if (row.get(SEASON).equals("WINTER")) {
                filteredList.add(row);
            }

            if (row.get(NAME_CATEGORY).equals("FORMAL") && isFormal) {
                formalList.add(row);
            }

        }
    }

    private Map<String, String> getRandomItem(List<Map<String, String>> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private void processSpecificSelection(String type) {
        Map<String, List<Map<String, String>>> categoryMap = Map.of(
                SUPERIOR, temporalTop,
                INFERIOR, temporalUnder,
                ABRIGO, temporalCoat,
                ACCESORIO, temporalAccesory,
                CUERPO_COMPLETO, temporalFullBody,
                CALZADO, temporalFootwear
        );

        // Verificar si la categoría está en el mapa y si su lista no está vacía
        if (categoryMap.containsKey(type) && !categoryMap.get(type).isEmpty()) {
            categories.put(type, categoryMap.get(type));
            selectRandomFromList(categoryMap.get(type), type, false);
        }
    }


    private void addClothing(Map<String, String> selectedItem, boolean flag) {

        Outfit item = new Outfit();
        Category cat = new Category();
        cat.setId(Long.valueOf(selectedItem.get(ID_CATEGORY)));
        cat.setName(CategoryEnum.valueOf(selectedItem.get(NAME_CATEGORY)));
        item.setId(Integer.valueOf(selectedItem.get(ID)));
        item.setImageUrl(selectedItem.get(IMAGE_URL));
        item.setCategory(cat);
        outfit.add(item);
        if(!selectionType.equals("random") && flag){


            Outfit o = new Outfit();
            for (int i = 0; i < outfit.size(); i++){
                Clothing c = new Clothing();
                c.setId(Long.valueOf(outfit.get(i).getId()));
                c.setImageUrl(outfit.get(i).getImageUrl());
                o.setCategory(outfit.get(i).getCategory());
                o.getClothing().add(c);
            }
            weeklyOutfit.add(o);

            outfit = new ArrayList<>();
        }

    }

    private Map<String, String> addRow(Map<String, Object> row, String type) {
        Map<String,String> result = new HashMap<>();
        result.put(ID, String.valueOf(row.get(ID)));
        result.put(SUB_TYPE, (String) row.get(SUB_TYPE));
        result.put(COLOR,(String) row.get(COLOR));
        result.put(IMAGE_URL, (String) row.get(IMAGE_URL));
        result.put(TYPE,type);
        result.put(ID_CATEGORY, String.valueOf(row.get(ID_CATEGORY)));
        result.put(NAME_CATEGORY,(String) row.get(NAME_CATEGORY));
        result.put(SEASON,(String) row.get(SEASON));
        return result;
    }


        public List<Clothing> generateTrendingOutfit(List<Map<String, Object>> temporal) {
            // Agrupa las prendas por categoría
            categorizeClothing(temporal);

            // Ordena las listas por tamaño (simulando popularidad)
            sortCategoriesBySize();

            // Genera el outfit basado en las categorías más populares
            Random random = new Random();
            outfit = new ArrayList<>();

            selectTrendingClothing(categories.get(SUPERIOR), random);
            selectTrendingClothing(categories.get(INFERIOR), random);
            selectTrendingClothing(categories.get(ABRIGO), random);
            selectTrendingClothing(categories.get(CALZADO), random);
            selectTrendingClothing(categories.get(ACCESORIO), random);

            return outfit;
        }

        private void sortCategoriesBySize() {
            // Ordena cada lista en el mapa por tamaño
            categories.values().forEach(list -> list.sort((a, b) -> Integer.compare(a.size(), b.size())));
        }

        private void selectTrendingClothing(List<Map<String, String>> items, Random random) {
            if (items == null || items.isEmpty()) {
                System.out.println("No hay prendas en esta categoría.");
                return;
            }

            // Selección de un elemento de los 3 más populares
            Map<String, String> selectedItem = items.get(random.nextInt(Math.min(items.size(), 3)));
            items.remove(selectedItem);

            Clothing clothing = new Clothing();
            clothing.setId(Long.valueOf(selectedItem.get(ID)));
            clothing.setImageUrl(selectedItem.get(IMAGE_URL));

            outfit.add(clothing);
        }

        private void categorizeClothing(List<Map<String, Object>> temporal) {
            // Inicialización de categorías
            categories.put(SUPERIOR, new ArrayList<>());
            categories.put(INFERIOR, new ArrayList<>());
            categories.put(ABRIGO, new ArrayList<>());
            categories.put(CALZADO, new ArrayList<>());
            categories.put(ACCESORIO, new ArrayList<>());

            // Clasificación de las prendas
            for (Map<String, Object> row : temporal) {
                String type = (String) row.get(TYPE);
                if (categories.containsKey(type)) {
                    categories.get(type).add(addRow(row));
                }
            }

            // Logs para depuración
            categories.forEach((key, value) -> {
                System.out.println("Categoría: " + key + ", elementos: " + value.size());
            });
        }

        private Map<String, String> addRow(Map<String, Object> row) {
            Map<String, String> result = new HashMap<>();
            result.put(ID, String.valueOf(row.get(ID)));
            result.put(IMAGE_URL, (String) row.get(IMAGE_URL));
            result.put(TYPE, (String) row.get(TYPE));
            return result;
        }
    }