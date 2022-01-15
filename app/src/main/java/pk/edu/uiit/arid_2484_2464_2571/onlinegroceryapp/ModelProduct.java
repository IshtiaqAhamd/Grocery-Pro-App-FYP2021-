package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

public class ModelProduct {
    String Product_ID, Product_Title, Product_Description, Product_Category, Product_Quantity,
            Product_Icon, Product_Price, Discount_Price, Discount_Note,
            Discount_Available, Time_Stamp, User_ID, Original_Price;

    public ModelProduct() {
    }

    public ModelProduct(String product_ID, String product_Title, String product_Description, String product_Category,
                        String product_Quantity, String product_Icon, String product_Price, String discount_Price,
                        String discount_Note, String discount_Available, String time_Stamp, String user_ID, String Original_Price)
    {
        this.Product_ID = product_ID;
        this.Product_Title = product_Title;
        this.Product_Description = product_Description;
        this.Product_Category = product_Category;
        this.Product_Quantity = product_Quantity;
        this.Product_Icon = product_Icon;
        this.Product_Price = product_Price;
        this.Discount_Price = discount_Price;
        this.Discount_Note = discount_Note;
        this.Discount_Available = discount_Available;
        this.Time_Stamp = time_Stamp;
        this.User_ID = user_ID;
        this.Original_Price = Original_Price;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_Title() {
        return Product_Title;
    }

    public void setProduct_Title(String product_Title) {
        Product_Title = product_Title;
    }

    public String getProduct_Description() {
        return Product_Description;
    }

    public void setProduct_Description(String product_Description) {
        Product_Description = product_Description;
    }

    public String getProduct_Category() {
        return Product_Category;
    }

    public void setProduct_Category(String product_Category) {
        Product_Category = product_Category;
    }

    public String getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_Quantity(String product_Quantity) {
        Product_Quantity = product_Quantity;
    }

    public String getProduct_Icon() {
        return Product_Icon;
    }

    public void setProduct_Icon(String product_Icon) {
        Product_Icon = product_Icon;
    }

    public String getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(String product_Price) {
        Product_Price = product_Price;
    }

    public String getDiscount_Price() {
        return Discount_Price;
    }

    public void setDiscount_Price(String discount_Price) {
        Discount_Price = discount_Price;
    }

    public String getDiscount_Note() {
        return Discount_Note;
    }

    public void setDiscount_Note(String discount_Note) {
        Discount_Note = discount_Note;
    }

    public String getDiscount_Available() {
        return Discount_Available;
    }

    public void setDiscount_Available(String discount_Available) {
        Discount_Available = discount_Available;
    }

    public String getTime_Stamp() {
        return Time_Stamp;
    }

    public void setTime_Stamp(String time_Stamp) {
        Time_Stamp = time_Stamp;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) { User_ID = user_ID;

    }
    public String getOriginal_Price() {
        return Original_Price;
    }

    public void setOriginal_Price(String original_Price) {
        Original_Price = original_Price;
    }
}
