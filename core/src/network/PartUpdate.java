package network;

import com.badlogic.gdx.utils.Pool;

public class PartUpdate implements Pool.Poolable{
    public byte x;
    public byte y;
    public byte rotation;
    public byte part;
    public byte id;

    @Override
    public void reset() {

    }
//    public byte[] x;
//    public byte[] y;
//    public byte[] rotation;
//    public byte[] part;
//    public byte[] id;
//
//    public PartUpdate() {
//    }
//
//    public PartUpdate set( byte players ){
//        if(x != null)
//            x = new byte[players];
//        if(y != null)
//            y = new byte[players];
//        if(rotation != null)
//            rotation = new byte[players];
//        if(part != null)
//            part = new byte[players];
//        if(id != null)
//            id = new byte[players];
//
//        return this;
//    }
//
//
//    @Override
//    public void reset() {
//        if(x != null)
//            for(int i = 0; i < x.length; i++)
//                x[i] = 0;
//        if(y != null)
//            for(int i = 0; i < y.length; i++)
//                y[i] = 0;
//        if(rotation != null)
//            for(int i = 0; i < rotation.length; i++)
//                rotation[i] = 0;
//        if(part != null)
//            for(int i = 0; i < part.length; i++)
//                part[i] = 0;
//        if(id != null)
//            for(int i = 0; i < id.length; i++)
//                id[i] = -1;
//    }


}
