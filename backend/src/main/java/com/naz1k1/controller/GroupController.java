package com.naz1k1.controller;

import com.naz1k1.entity.Group;
import com.naz1k1.entity.GroupMember;
import com.naz1k1.model.request.CreateGroupRequest;
import com.naz1k1.model.request.UpdateGroupRequest;
import com.naz1k1.model.response.GroupVO;
import com.naz1k1.model.response.Result;
import com.naz1k1.service.GroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public Result<List<GroupVO>> getUserGroups(@RequestAttribute Long userId) {
        List<GroupVO> groups = groupService.getUserGroups(userId);
        return Result.success(groups);
    }

    @PostMapping
    public Result<Group> createGroup(
            @Validated @RequestBody CreateGroupRequest request,
            @RequestAttribute Long userId) {
        Group group = groupService.createGroup(request, userId);
        return Result.success(group);
    }

    @PutMapping
    public Result<Void> updateGroup(
            @Validated @RequestBody UpdateGroupRequest request,
            @RequestAttribute Long userId) {
        groupService.updateGroup(request, userId);
        return Result.success();
    }

    @DeleteMapping("/{groupId}")
    public Result<Void> dissolveGroup(
            @PathVariable Long groupId,
            @RequestAttribute Long userId) {
        groupService.dissolveGroup(groupId, userId);
        return Result.success();
    }

    @GetMapping("/{groupId}")
    public Result<Group> getGroupInfo(@PathVariable Long groupId) {
        Group group = groupService.getById(groupId);
        if (group == null) {
            return Result.failed();
        }
        return Result.success(group);
    }

    @GetMapping("/{groupId}/members")
    public Result<List<GroupMember>> getGroupMembers(@PathVariable Long groupId) {
        List<GroupMember> members = groupService.getGroupMembers(groupId);
        return Result.success(members);
    }
} 