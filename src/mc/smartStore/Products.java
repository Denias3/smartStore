package mc.smartStore;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Products {
    private static smartStore plugin;
    private static File fileProduct;
    public static FileConfiguration product;

    public static void init(smartStore plugin) {
        Products.plugin = plugin;
        start();
    }

    public static FileConfiguration start ()
    {
        fileProduct = new File(plugin.getDataFolder() + File.separator + "product.yml");
        product = YamlConfiguration.loadConfiguration(fileProduct);
        return product;
    }

    public static FileConfiguration getProduct () { return product; }

    public static void setPrice(Material mat, int price){
        product.set(mat.name()+".price", price);
        try{
            product.save(fileProduct);
        }catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }
    public static void setPrice(String mat, int price){
        product.set(mat+".price", price);
        try{
            product.save(fileProduct);
        }catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }
    public static void setCount(Material mat, int count) {
        product.set(mat.name()+".count", count);
        try{
            product.save(fileProduct);
        }catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }
    public static void setCount(String mat, int count) {
        product.set(mat+".count", count);
        try{
            product.save(fileProduct);
        }catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    public static int getPrice(Material mat) { return product.getInt(mat.name()+".price"); }
    public static int getMaxPrice(Material mat) { return product.getInt(mat.name()+".max_price"); }
    public static int getMinPrice(Material mat) { return product.getInt(mat.name()+".min_price"); }
    public static int getCount(Material mat) { return product.getInt(mat.name()+".count"); }
    public static int getMaxCount(Material mat) { return product.getInt(mat.name()+".max_count"); }
    public static int getStep(Material mat) { return product.getInt(mat.name()+".step"); }

    public static int getPrice(String mat) { return product.getInt(mat+".price"); }
    public static int getMaxPrice(String mat) { return product.getInt(mat+".max_price"); }
    public static int getMinPrice(String mat) { return product.getInt(mat+".min_price"); }
    public static int getCount(String mat) { return product.getInt(mat+".count"); }
    public static int getMaxCount(String mat) { return product.getInt(mat+".max_count"); }
    public static int getStep(String mat) { return product.getInt(mat+".step"); }




    public static void bust (String []items)
    {
        for (int i = 0; i < items.length; i++) {
            if (product.contains(items[i])) {
                int maxCount = Products.getMaxCount(items[i]);
                int count = Products.getCount(items[i]);
                int price = Products.getPrice(items[i]);
                int step = Products.getStep(items[i]);
                double percent30 = maxCount * 0.30;
                double percent70 = maxCount * 0.70;

                if (count < percent30)
                {
                    int minPrice = Products.getMinPrice(items[i]);
                    Products.setPrice(items[i], Math.max(price - step, minPrice));
                }
                else if (count > percent70)
                {
                    int maxPrice = Products.getMaxPrice(items[i]);
                    Products.setPrice(items[i], Math.min(price + step, maxPrice));
                }
                Products.setCount(items[i], count);
            }
        }
    }
}
