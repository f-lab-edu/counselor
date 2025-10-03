package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.common.eNum.CounselType;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hyejin.counselor.core.common.util.DateUtil.nowDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsel")
public class CounselController {

    private final CounselService counselService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> counselSave(@RequestBody Counsel counsel) {
        counsel = counselService.createCounsel(counsel);
        counselService.save(counsel);
        return ResponseEntity.ok(ApiResponse.success(counsel));
    }
}
