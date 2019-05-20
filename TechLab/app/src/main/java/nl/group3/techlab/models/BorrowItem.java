package nl.group3.techlab.models;

//import com.example.borrow.models.User;

public class BorrowItem {
//    public String firstName;
    public int ItemID;

    public BorrowItem(int fID){
        ItemID = fID;
//        firstName = user;
    }
    public int getID() {
        return ItemID;
    }

//    public String getFirstName() {
//        return firstName;
//    }
}
