package mc.smartStore;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class StoreItems {


    private double basePrice = -1;
    private int count = 0 ;
    private int place = -1;

    public double getDerivative() {
        return derivative;
    }

    private double derivative;

    Configuration cfg = SmartStore.getPlugin().getConfig();
    public StoreItems(ItemStack item, int place){
        this.item = item;
        this.item.setAmount(1);
        this.item.removeItemFlags();
        this.place = place;
    }
    public StoreItems(String name, double basePrice, int count, int place){
        if (name == null)
            return;
        Material mater = Material.getMaterial(name);
        if (mater == null)
            return;
        this.item = new ItemStack(mater);
        setBasePrice(basePrice);
        this.item.setAmount(1);
        this.count = count;
        this.place = place;

    }
    public void clearStats(){
        basePrice = -1;
        count = 0 ;
        place = -1;
    }

    private double WarehousePercentage (){
        // Беру данные Коэффициентов
//        double cfgBuy = cfg.getDouble("coefficients.buy"); // Покупки Buy
        double cfgSell = cfg.getDouble("coefficients.sell"); // Покупки sell
        double cfgWH = cfg.getDouble("coefficients.warehouse"); // Коэффициент склада
        // сам расчет
        if (count/(cfgWH / basePrice) * (cfgSell - 0.1)<= (cfgSell - 0.1)){
//            Message.toConsole("Start");
//            Message.toConsole(String.format("%.10f",cfgWH / basePrice)+ "cfgWH / basePrice");
//            Message.toConsole(String.format("%.10f",cfgSell - 0.1)+ "cfgSell - 0.1");
//            Message.toConsole(String.format("%.10f",(double)count / (cfgWH / basePrice))+ "(double)count / (cfgWH / basePrice)");
//            Message.toConsole(String.format("%.10f",cfgWH / basePrice)+ "cfgWH / basePrice");
            return (double)count / (cfgWH / basePrice) * (cfgSell - 0.1);
        }
        else {
            return (cfgSell - 0.1);
        }
    }



    public double getBuyOne(){
        double cfgBuy = cfg.getDouble("coefficients.buy");
        return (basePrice * (cfgBuy - WarehousePercentage()));
    }
    public double getSellOne(){
        double cfgSell = cfg.getDouble("coefficients.sell");
        return (basePrice * (cfgSell - WarehousePercentage()));
    }

    public double getBuyMore(int size){
        double buy= getBuyOne();
        int fSize = (int )Math.min(count - (cfg.getInt("coefficients.warehouse") / basePrice), size);
        if (fSize < 0)
            fSize = 0;
        return ((buy * size) + (derivative * ((1.0/2.0*(size - fSize-1)*((size - fSize))))) );
    }
    public double getSellMore(int size){
        double sell = getSellOne();
        int fSize = (int )Math.min((cfg.getInt("coefficients.warehouse") / basePrice) - count , size);
        if (fSize < 0)
            fSize = 0;
//        Message.toConsole(fSize +" fSize");
//        Message.toConsole(size +" size");
        return ((sell * size) - (derivative * ((1.0/2.0*(fSize-1)*((fSize)))+ (fSize * (size - fSize)))) );
    }


    public void print(){
        Message.toConsole("================================");
        if(item != null)
            Message.toConsole("name item: " +item.getType().name());
        else
            Message.toConsole("name item: null");
        Message.toConsole("place: " +place);
        Message.toConsole("base price: " +basePrice);
        Message.toConsole("count: " +count );
    }
    public boolean check(){

        if (basePrice == -1){
            return true;
        }
        else if (count == -1){
            return true;
        }
        else
            return false;

    }
    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    private ItemStack item;


    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double price) {
        this.basePrice = price;
        double cfgSell = cfg.getDouble("coefficients.sell");
        this.derivative = (basePrice * cfgSell) - ( basePrice*( cfgSell - ((double)1 /(cfg.getDouble("coefficients.warehouse")/basePrice) * (cfg.getDouble("coefficients.sell") -  0.1 ))));
//        Message.toConsole(String.format("%.10f",basePrice)+"");
//        Message.toConsole(String.format("%.10f",derivative)+"");
    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

}
