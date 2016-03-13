package com.madebydragons.elocalculator;


public class EloCalculator {
    // TODO: replace with configurable K
    // TODO: make single player also work through this model
    private static double K=16;

    // a looses to b
    public static int loose(int a, int b){
        return new Double(points(a,b,0)).intValue();
    }

    // a wins to b
    public static int win(int a, int b){
        return new Double(points(a,b,1)).intValue();

    }

    // a draw with b
    public static int draw(int a, int b){
        return new Double(points(a,b,0.5)).intValue();
    }

    // final elo of a given matches such that in elos[i] the outcome, in points was outcomes[i]
    public static double tournament(int initial, Integer elos[],Double outcomes[]) throws InvalidTournamentData{
        if(elos.length != outcomes.length)
            throw new InvalidTournamentData("inconsistent elo/outcomes lengths.");
        double p = 0.0;
        for(int i=0;i<elos.length;i++) p += points(initial, elos[i].intValue(), outcomes[i].doubleValue());
        return Math.round(  p + (double) initial );
    }


    public static double points(double p1,double p2,double actual){
        double expected_p1 = 1.0 / (1 + Math.pow(10.0, (p2-p1)/400.0));
        //TODO: make K configurable from settings
        //TODO: verify Math.round rounding is appropriate
        return Math.round(K*(actual - expected_p1));
    }
}

