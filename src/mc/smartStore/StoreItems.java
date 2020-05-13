package mc.smartStore;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StoreItems {


    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    private ItemStack item;

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    private double maxPrice = -1;
    private double minPrice = -1;
    private double price = -1;
    private int maxCount = -1;
    private int count = -1 ;
    private double step = -1;
    private int place = -1;

    public StoreItems(ItemStack item, int place){
        this.item = item;
        this.item.setAmount(1);
        this.item.removeItemFlags();
        this.place = place;
    }
    public StoreItems(String name, int maxPrice, int minPrice, int price, int maxCount, int count, int step, int place){
        if (name == null)
            return;
        Material mater = Material.getMaterial(name);
        if (mater == null)
            return;
        this.item = new ItemStack(mater);
        this.item.setAmount(1);
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.price =price;
        this.maxCount = maxCount;
        this.count = count;
        this.step = step;
        this.place = place;
    }
    public void clearStats(){
        maxPrice = -1;
        minPrice = -1;
        price = -1;
        maxCount = -1;
        count = -1 ;
        step = -1;
        place = -1;
    }

    public void print(){
        Message.toConsole("================================");
        if(item != null)
            Message.toConsole("name item: " +item.getType().name());
        else
            Message.toConsole("name item: null");
        Message.toConsole("place: " +place);
        Message.toConsole("maxPrice: " +maxPrice);
        Message.toConsole("minPrice: " +minPrice);
        Message.toConsole("price: " +price);
        Message.toConsole("maxCount: " +maxCount);
        Message.toConsole("count: " +count );
        Message.toConsole("step: " +step);
    }
    public boolean check(){
        if (maxPrice == -1){
            return true;
        }
        else if (minPrice == -1){
            return true;
        }
        else if (price == -1){
            return true;
        }
        else if (maxCount == -1){
            return true;
        }
        else if (count == -1){
            return true;
        }
        else if (step == -1){
            return true;
        }
        else
            return false;

    }
}
