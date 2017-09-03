package info.devexchanges.ratingbarlistview.Utilities;

import android.app.Application;
import android.test.mock.MockDialogInterface;
import android.util.Log;

import java.util.ArrayList;

import info.devexchanges.ratingbarlistview.Movie;

/**
 * Created by defcon on 03/09/17.
 */

public class ApplicationClass extends Application {
    public ArrayList<Movie> listToSend = new ArrayList<>();

    public Integer addItem(Movie item){
        Integer newIndex = listToSend.size();
        printList();
        item.setGlobalArrayIndex(newIndex);
        listToSend.add(item);
        Log.d("ADDITEM","Adding " + item.getName() + "With rating " + item.getRatingStar());
        return newIndex;
    }

    public ArrayList<Movie> getList(){
        return listToSend;
    }

    public void removeItem(Integer index){
        listToSend.get(index).setRatingStar((float) 0.0);
        Log.d("RM", String.valueOf(listToSend.remove(index)));
//         printList();
    }

    public void printList(){
        for (Movie item : listToSend) {
            Log.d("PrintList" , item.getName() + " : " + item.getRatingStar());
        }
    }

    public Integer getSizeToSend(){
        Integer size = 0 ;
        for (Movie item : listToSend){
            if (item.getRatingStar() != 0.0) {
                size += 1 ;
            }
        }
        return size ;
    }
}
