package com.example.rtb.web;

import com.example.rtb.entity.Campaign;
import com.example.rtb.repository.CampaignRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    private final CampaignRepository campaignRepository;

    public DashboardController(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("campaign", new Campaign());
        model.addAttribute("campaigns", campaignRepository.findTop50ByOrderByIdDesc());
        return "dashboard";
    }

    @PostMapping("/campaigns")
    public String saveCampaign(@ModelAttribute Campaign campaign) {
        campaignRepository.save(campaign);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
