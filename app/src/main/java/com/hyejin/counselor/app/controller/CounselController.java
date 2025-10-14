package com.hyejin.counselor.app.controller;

import com.hyejin.counselor.app.common.ApiResponse;
import com.hyejin.counselor.core.dto.CounselCursorRequest;
import com.hyejin.counselor.core.entity.Counsel;
import com.hyejin.counselor.core.entity.User;
import com.hyejin.counselor.core.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsel")
public class CounselController {

    private final CounselService counselService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> counselList(@RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "") String lastRegDate) {
        CounselCursorRequest cursorRequest = counselService.createCounselCursorRequest(status, lastRegDate);
        return ResponseEntity.ok(ApiResponse.success(counselService.counselList(cursorRequest)));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> counselSave(@RequestBody Counsel counsel) {
        counsel = counselService.createCounsel(counsel);
        counselService.save(counsel);
        return ResponseEntity.ok(ApiResponse.success(counsel));
    }
  
    @GetMapping("/{counselId}/user")
    public ResponseEntity<ApiResponse<Object>> userSearch(@PathVariable String counselId) throws Exception {
        User userInfo = counselService.userSearch(counselId);
        return ResponseEntity.ok(ApiResponse.success(userInfo));
    }

}
