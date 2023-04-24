package com.console.mall.controller;

import com.console.mall.dto.PaginationDTO;
import com.console.mall.entitiy.*;
import com.console.mall.service.CategoryService;
import com.console.mall.service.ItemService;
import com.console.mall.service.ReviewService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ReviewService reviewService;

    @GetMapping("/item")
    public String itemShow(@RequestParam(value = "id") Long id, Model model, HttpSession session) {

        List<Item> itemList = itemService.findAllItem(id);

        session.setAttribute("itemList", itemList);
        PaginationDTO paginationDTO = new PaginationDTO(1, itemList.size());
        model.addAttribute("pagination", paginationDTO);

        return "item_show";
    }

    @GetMapping("/item/pagination")
    public String pagination(@RequestParam("page") int page, @RequestParam("totalPages") int totalPages, Model model) {
        PaginationDTO paginationDTO = new PaginationDTO(page, totalPages);
        model.addAttribute("pagination", paginationDTO);
        return "item_show";
    }

    @GetMapping("/testAPI")
    public String testAPI(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Client-ID", "0dle430ej7oslhsv235qjsf4m49hch");
        headers.set("Authorization", "Bearer cvit9m38vcnnduyjxsyc9xthnsf656");

        String url = "https://api.igdb.com/v4/games";
        String query = "fields name, platforms.name, cover.url, summary, total_rating; where release_dates.platform = (48, 167); sort total_rating desc; limit 10;";
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();

        // Gson 객체 생성
        Gson gson = new Gson();

        // JSON 문자열을 Game 리스트로 파싱
        Type gameListType = new TypeToken<List<Game>>(){}.getType();
        List<Game> games = gson.fromJson(responseBody, gameListType);

        // Model에 games 리스트를 추가하여 View로 전달
        model.addAttribute("games", games);

        return "testAPI";
    }



    @GetMapping("/item/info/{id}")
    public String itemInfo(@PathVariable("id") Long id, Model model) {
        Item item = itemService.findOneItem(id);
        model.addAttribute("item", item);
        List<Review> reviewList = item.getList();
        System.out.println("review size" + reviewList.size());
        model.addAttribute("reviewList", reviewList);
        return "item_info";
    }

    @GetMapping("/admin/item-add")
    public String itemAdd(Model model) {
        List<Category> categoryList = categoryService.findAllCategory();


        model.addAttribute("categoryList", categoryList);

        return "item_admin";
    }

    @PostMapping("/admin/item-save")
    @ResponseBody
    public String adminAddItem(@RequestParam("name") String name,
                               @RequestParam("price") int price,
                               @RequestParam("stockQuantity") int stockQuantity,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("itemInfo") String itemInfo,
                               @RequestParam("itemVideo") String itemVideo,
                               @RequestParam("category_id") Long id) throws IOException {

        byte[] bytes = file.getBytes();
        String decodedFileName = URLDecoder.decode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        Path path = Paths.get("src/main/resources/static/img/" + decodedFileName.replace("\\", "/"));
        Files.write(path, bytes);

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setImage("/img/" + decodedFileName);
        item.setItemInfo(itemInfo);
        item.setItemVideo(itemVideo);
        Category category = categoryService.findOneCategory(id);
        item.setCategory(category);
        System.out.println("item.image = " + item.getImage());
        itemService.saveItem(item);
        return null;
    }

}
