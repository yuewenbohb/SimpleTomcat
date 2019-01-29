package homework;

import java.util.*;

public class HomeWork {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("1", "4");
        map.put("2", "3");
        map.put("3", "2");
        map.put("4", "1");
        map.put("5", "0");
        map.put("6", "9");
        map.put("7", "8");

        Map<String, String> map2 = new HashMap<>();
        map2.put("1", "4");
        map2.put("2", "3");
        map2.put("3", "2");
        map2.put("4", "1");
        map2.put("5", "0");
        map2.put("6", "9");
        map2.put("7", "8");

        List<Map<String, String>> list = new ArrayList<>();
        list.add(map);
        list.add(map2);

        Iterator<Map<String, String>> it = list.iterator();
        while (it.hasNext()) {
            Map<String, String> t = it.next();
            for (Map.Entry<String, String> entry : t.entrySet()) {
                System.out.println(entry.getKey() + "---" + entry.getValue());
            }
        }

        Map<String, List<String>> map3 = new HashMap<>();
        List<String> l1 = new ArrayList<>();
        l1.add("123");
        l1.add("1234");
        l1.add("1235");
        l1.add("1236");
        List<String> l12 = new ArrayList<>();
        l12.add("123");
        l12.add("1234");
        l12.add("1235");
        l12.add("1236");

        map3.put("tom", l1);
        map3.put("Jerry", l12);

        for (Map.Entry<String, List<String>> entry : map3.entrySet()) {
            Iterator<String> it2 = entry.getValue().iterator();
            while (it2.hasNext()){
                System.out.println(it2.next());
            }
        }

    }
}
