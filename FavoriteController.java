package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller
public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
	private final HouseRepository houseRepository;
	
	public FavoriteController (FavoriteRepository favoriteRepository, FavoriteService favoriteService, HouseRepository houseRepository) {
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
		this.houseRepository = houseRepository;
	}
	
	//お気に入り一覧表示
	@GetMapping("/favorites")
	public String index (@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable, Model model) {
        User user = userDetailsImpl.getUser();
        Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);  //Page<T> ←エンティティ
		model.addAttribute("favoritePage", favoritePage);
		return "favorites/index";
	}
	
	//お気に入り登録
	@PostMapping("/houses/{house_id}/favorites/register")
	public String register (@PathVariable(name = "houseId") Integer houseId, RedirectAttributes redirectAttributes, Model model){
		House house = houseRepository.getReferenceById(houseId);
		model.addAttribute("house", house);
		return "redirect:/houses/{house_id}";

	}
	
	//お気に入り解除
	@PostMapping("/houses/{house_id}/favorites/{favorites}/{favorite_id}/delete")
	public String delete (@PathVariable(name = "favoriteId") Integer favoriteId, RedirectAttributes redirectAttributes, Model model) {
		favoriteRepository.deleteById(favoriteId);

		return "redirect:/houses/{house_id}";
	}
}
