package db;

import java.util.ArrayList;

/**
 * Created by Егор on 22.04.2015.
 */
class MyArrayList<T> extends ArrayList<String[]>
{
    public MyArrayList() {
        // TODO Auto-generated constructor stub
        super();
    }
    public MyArrayList(int size) {
        // TODO Auto-generated constructor stub
        super(size);
    }
    @Override
    public int indexOf(Object arg0) {
        // TODO Auto-generated method stub
        String[] a = (String[])arg0;
        for (int i = size() - 1; i >= 0; i--)
            if(get(i)[0].equals(a[0]) && get(i)[1].equals(a[1]))
                return i;
        return -1;
    }
}