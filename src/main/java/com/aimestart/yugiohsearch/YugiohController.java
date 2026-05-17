package com.aimestart.yugiohsearch;
import com.aimestart.yugiohsearch.YugiohService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/yugioh")
public class YugiohController {

    private final YugiohService yugiohService;

    public YugiohController(YugiohService yugiohService) {
        this.yugiohService = yugiohService;

    }

    /*@GetMapping("/card")
    public String getCardDescription(@RequestParam String name) {
        return yugiohService.getCardDescription(name);
    }
    @GetMapping("/all")
    public List<YugiohService.CardData> getAllCards(){
        return yugiohService.fetchallCards();
    }*/
    @PostMapping("/import")
    public List<Card> importAllCards(){
        return yugiohService.importAllCards();
    }
}
