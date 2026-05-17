package com.aimestart.yugiohsearch;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class YugiohService {

    private final RestClient restClient;
    private final CardRepository cardRepository;

    public record CardInfoResponse(List<CardData> data) {}

    public record CardData(String name, String desc) {}

    public YugiohService(RestClient.Builder builder, CardRepository cardRepository) {
        this.restClient = builder.baseUrl("https://db.ygoprodeck.com/api/v7").build();
        this.cardRepository = cardRepository;
    }

   /* public String getCardDescription(String name) {
        CardInfoResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cardinfo.php")
                        .queryParam("name", name)
                        .build())
                .retrieve()
                .body(CardInfoResponse.class);

        if (response == null || response.data() == null || response.data().isEmpty()) {
            throw new RuntimeException("Card not found");
        }

        return response.data().get(0).desc();
    }*/

    public List<CardData> fetchallCards(){
        CardInfoResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cardinfo.php")
                        .build())
                        .retrieve()
                        .body(CardInfoResponse.class);

        if (response == null || response.data() == null || response.data().isEmpty()) {
            throw new RuntimeException("Cards not found");
        }
        return response.data();
    }
    public List<Card> importAllCards(){
       List<CardData> apiCards = fetchallCards();
       List<Card> cardsToSave = new ArrayList<>();

       for(CardData card:apiCards){
           if(!cardRepository.existsByName(card.name())){
               cardsToSave.add(new Card(card.name(), card.desc()));
           }
       }
       return cardRepository.saveAll(cardsToSave);
    }
}
