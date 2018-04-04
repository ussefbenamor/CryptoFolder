package com.nafaaazaiez.cryptofolder.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ussef on 28/01/2018.
 */

public class ConvertResponse {


    @SerializedName("EUR")
    @Expose
    private Double eUR;

    public Double getEUR() {
        return eUR;
    }

    public void setEUR(Double eUR) {
        this.eUR = eUR;
    }

    @Override
    public String toString() {
        return String.valueOf(eUR);
    }


}
