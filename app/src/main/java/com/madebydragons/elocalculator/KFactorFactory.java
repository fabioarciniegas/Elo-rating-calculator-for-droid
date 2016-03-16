package com.madebydragons.elocalculator;
import android.content.Context;

/**
 * A factory that creates an appropriate KFactor class based on a unique identifier.
 * Ids defined in strings.xml
 *
 */
public class KFactorFactory {

    private Context mContext;

    public KFactorFactory(Context c){
        mContext = c;
    }

   public KFactor createKFactor(String id) throws UnknownKFactorIdentifier{

        if(id.equals(mContext.getString(R.string.k_factor_chess_com)))
            return new KFactor(){
                @Override
                public double K(int elo){
                    return 16.0;
                }
            };
       else if(id.equals(mContext.getString(R.string.k_factor_icc)))
           return new KFactor(){
               @Override
               public double K(int elo){
                   //TODO: obviously wrong, just for kicks
                   return 1.0;
               }
           };
       else throw new UnknownKFactorIdentifier(id);
   }
}
