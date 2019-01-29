package homework;

import java.io.File;

public class HomeWork22 {
    public static void main(String[] args) {
        File file = new File("D:\\WP\\王道\\homework\\sdf\\");
        DirectionWork.del(file);

    }

    static class DirectionWork {
        static void del(File file) {
            if (!file.exists())
                return;
            if (!file.isDirectory()) {
                file.delete();
                return;
            }
            for (File temp : file.listFiles()) {
                del(temp);
            }
            file.delete();
            return;
        }

        static void del(String path) {
            DirectionWork.del(new File(path));
        }
    }

}
