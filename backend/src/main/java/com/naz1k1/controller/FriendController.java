package com.naz1k1.controller;

import com.naz1k1.model.R;
import com.naz1k1.model.dto.friend.FriendRequestDTO;
import com.naz1k1.model.vo.friend.FriendRequestVO;
import com.naz1k1.model.vo.friend.FriendVO;
import com.naz1k1.service.FriendNotificationService;
import com.naz1k1.service.FriendRelationshipService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendNotificationService friendNotificationService;
    private final FriendRelationshipService friendRelationshipService;

    public FriendController(FriendNotificationService friendNotificationService,
                          FriendRelationshipService friendRelationshipService) {
        this.friendNotificationService = friendNotificationService;
        this.friendRelationshipService = friendRelationshipService;
    }

    /**
     * 获取好友列表
     */
    @GetMapping("/list")
    public R<List<FriendVO>> getFriendList(@RequestAttribute Long userId) {
        List<FriendVO> friendList = friendRelationshipService.getFriendList(userId);
        return R.success(friendList);
    }

    /**
     * 发送好友请求
     * @param userId 当前用户ID
     * @param dto 好友请求信息
     * @return 发送结果
     */
    @PostMapping("/requests")
    public R<?> sendFriendRequest(@RequestAttribute Long userId,
                                @Valid @RequestBody FriendRequestDTO dto) {
        friendRelationshipService.sendRequest(userId, dto);
        return R.success();
    }

    /**
     * 获取好友请求列表
     * @param userId 当前用户ID
     * @return 好友请求列表
     */
    @GetMapping("/requests")
    public R<List<FriendRequestVO>> getFriendRequests(@RequestAttribute Long userId) {
        List<FriendRequestVO> requests = friendNotificationService.getFriendRequests(userId);
        return R.success(requests);
    }

    /**
     * 处理好友请求
     * @param userId 当前用户ID
     * @param notificationId 好友请求通知ID
     * @param accept 是否接受请求
     * @return 处理结果
     */
    @PostMapping("/requests/{notificationId}/handle")
    public R<?> handleFriendRequest(@RequestAttribute Long userId,
                                  @PathVariable Long notificationId,
                                  @RequestParam boolean accept) {
        friendRelationshipService.handleFriendRequest(userId, notificationId, accept);
        return R.success();
    }
} 