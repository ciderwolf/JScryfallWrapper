package com.scyrfall.api.field;

import com.scyrfall.api.ScryfallObject;
import org.json.JSONObject;

import java.util.Objects;


/**
 * An object containing daily price information for this card, including usd, usd_foil, eur, and tix prices, as strings.
 */
public class Prices extends ScryfallObject {

    private String usdFoil, usd, eur, tix;

    public Prices(JSONObject data) {
        super(data);

        usd = getString("usd");
        usdFoil = getString("usd_foil");
        tix = getString("tix");
        eur = getString("eur");
    }

    /**
     * @return the lowest foil price for this card in US Dollars from Scryfall's affiliates, updated daily.
     */
    public String getUsdFoil() {
        return usdFoil;
    }

    /**
     * @return the lowest price for this card in US Dollars from Scryfall's affiliates, updated daily.
     */
    public String getUsd() {
        return usd;
    }

    /**
     * @return the lowest price for this card in Euros from Scryfall's affiliates, updated daily.
     */
    public String getEur() {
        return eur;
    }

    /**
     * @return the lowest price for this card in Magic Online Tickets from Scryfall's affiliates, updated daily.
     */
    public String getTix() {
        return tix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prices prices = (Prices) o;
        return Objects.equals(usdFoil, prices.usdFoil) &&
                Objects.equals(usd, prices.usd) &&
                Objects.equals(eur, prices.eur) &&
                Objects.equals(tix, prices.tix);
    }

    @Override
    public String toString() {
        return "Prices{" +
                "usdFoil='" + usdFoil + '\'' +
                ", usd='" + usd + '\'' +
                ", eur='" + eur + '\'' +
                ", tix='" + tix + '\'' +
                '}';
    }
}
