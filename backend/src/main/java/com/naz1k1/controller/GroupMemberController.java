package com.naz1k1.controller;

import com.naz1k1.model.response.Result;
import com.naz1k1.service.GroupMemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/groups/{groupId}/members")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    public GroupMemberController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @PostMapping
    public Result<Void> addMembers(
            @PathVariable Long groupId,
            @RequestBody @NotEmpty(message = "成员列表不能为空") List<Long> memberIds,
            @RequestAttribute Long userId) {
        groupMemberService.addMembers(groupId, memberIds, userId);
        return Result.success();
    }

    @DeleteMapping("/{memberId}")
    public Result<Void> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long memberId,
            @RequestAttribute Long userId) {
        groupMemberService.removeMember(groupId, memberId, userId);
        return Result.success();
    }

    @PutMapping("/{memberId}/role")
    public Result<Void> updateMemberRole(
            @PathVariable Long groupId,
            @PathVariable Long memberId,
            @RequestParam @NotNull(message = "角色不能为空") Integer role,
            @RequestAttribute Long userId) {
        groupMemberService.updateMemberRole(groupId, memberId, role, userId);
        return Result.success();
    }
} 