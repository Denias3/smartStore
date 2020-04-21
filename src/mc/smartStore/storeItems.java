package mc.smartStore;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class storeItems {


    public ItemStack getItem() {
        return item;
    }

    private ItemStack item;

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    private int maxPrice = -1;
    private int minPrice = -1;
    private int price = -1;
    private int maxCount = -1;
    private int count = -1 ;
    private int step = -1;
    private int place = -1;

    public storeItems (ItemStack item, int place){
        this.item = item;
        this.item.setAmount(1);
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

}
