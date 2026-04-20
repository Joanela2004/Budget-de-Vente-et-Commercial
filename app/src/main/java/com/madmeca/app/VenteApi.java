package com.madmeca.app;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface VenteApi {
   @GET("script.php")
   Call<List<Vente>> getVentes();

   @POST("script.php")
   Call<Void> addVente(@Body Vente vente);

   @PUT ("script.php")
   Call<Void> updateVente(@Body Vente vente);

   @DELETE("script.php")
   Call<Void> deleteVente(@Query ("annee") int annee);
   }