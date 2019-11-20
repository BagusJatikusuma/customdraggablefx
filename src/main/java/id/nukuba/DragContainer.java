package id.nukuba;

import javafx.scene.input.DataFormat;
import javafx.util.Pair;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DragContainer implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1458406119115196098L;

    public static final DataFormat AddNode =
            new DataFormat("application.DragIcon.add");

    private final List<Pair<String, String> > mDataPairs = new ArrayList<>();

    public DragContainer () {
    }

    public void addData (String key, String value) {
        mDataPairs.add(new Pair<String, String>(key, value));
    }

    public <T> T getValue (String key) {

        for (Pair<String, String> data: mDataPairs) {

            if (data.getKey().equals(key))
                return (T) data.getValue();

        }

        return null;
    }

    public List <Pair<String, String> > getData () { return mDataPairs; }
}
