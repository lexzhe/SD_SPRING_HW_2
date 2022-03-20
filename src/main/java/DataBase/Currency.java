package DataBase;

public enum Currency {
    RUB(1), EUR(120), USD(105);

    private final double relativeCoast;

    Currency(double relativeCoast) {
        this.relativeCoast = relativeCoast;
    }

    double getRelativeCoast() {
        return relativeCoast;
    }

    double convert(double amount, Currency to){
        return amount / getRelativeCoast() * to.getRelativeCoast();
    }
}
