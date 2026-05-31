package com.aimestart.yugiohsearch;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class YugiohService {

    private final RestClient restClient;
    private final CardRepository cardRepository;

    public record CardInfoResponse(List<CardData> data) {}

    public record CardData(String name, String desc, String type) {}

    public YugiohService(RestClient.Builder builder, CardRepository cardRepository) {
        this.restClient = builder.baseUrl("https://db.ygoprodeck.com/api/v7").build();
        this.cardRepository = cardRepository;
    }

    public List<CardData> fetchallCards() {
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

    public List<Card> importAllCards() {
        List<CardData> apiCards = fetchallCards();
        List<Card> cardsToSave = new ArrayList<>();
        for (CardData card : apiCards) {
            if (!cardRepository.existsByName(card.name())) {
                cardsToSave.add(new Card(card.name(), card.desc(), card.type(), 0));
            }
        }
        return cardRepository.saveAll(cardsToSave);
    }

    public void updateExistingCards() {
        List<Card> cards = cardRepository.findAll();
        List<CardData> apiCards = fetchallCards();
        for (Card card : cards) {
            for (CardData apiCard : apiCards) {
                if (card.getName().equals(apiCard.name())) {
                    String TYPE = apiCard.type();
                    card.setType(TYPE);
                    boolean switchwork = false;
                    switch(TYPE){
                        case "Spell Card":
                            card.setWeight(4);
                            switchwork = true;
                            break;
                        case "Trap Card":
                            card.setWeight(3);
                            switchwork = true;
                            break;
                        case "Effect Monster":
                            card.setWeight(2);
                            switchwork = true;
                            break;
                        case "Normal Monster":
                            card.setWeight(1);
                            switchwork = true;
                            break;
                        default:
                    }
                    if(!switchwork){
                        if(TYPE.contains("Spell")){
                            card.setWeight(4);
                        } else if (TYPE.contains("Trap")) {
                            card.setWeight(3);
                        } else if (TYPE.contains("Monster")) {
                            card.setWeight(2);
                        } else {
                            card.setWeight(1);
                        }
                    }
                    cardRepository.save(card);
                }
            }
        }
    }
    public Card getCardByName(String name) {
        return cardRepository.getCardByName(name);
    }

    public List<Card> getCardsBySubstring(String name) {
        String cardname = name;
        List<Card> cards = new ArrayList<>();
        for (Card card : cardRepository.findAll()) {
            if (card.getName().contains(cardname)) {
                cards.add(card);
            }
        }
        if (cards.isEmpty()) {
            throw new RuntimeException("No cards found with that name");
        }
        return cards;
    }

    public void AssigningWeight(){
        for (Card card : cardRepository.findAll()) {
            card.setWeight(1);
            cardRepository.save(card);
        }
    }
}
