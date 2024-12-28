//package com.example.chatapp.service;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class ChatGroupService {
//
//    private final ChatGroupRepository chatGroupRepository;
//    private final ChatGroupMemberRepository chatGroupMemberRepository;
//
//    public ChatGroup createGroup(String groupName, String createdBy) {
//        ChatGroup group = ChatGroup.builder()
//                .name(groupName)
//                .createdBy(createdBy)
//                .build();
//        return chatGroupRepository.save(group);
//    }
//
//    public void addUserToGroup(Long groupId, Long userId) {
//        ChatGroupMember member = ChatGroupMember.builder()
//                .groupId(groupId)
//                .userId(userId)
//                .build();
//        chatGroupMemberRepository.save(member);
//    }
//
//    public List<ChatGroup> getUserGroups(Long userId) {
//        return chatGroupMemberRepository.findGroupsByUserId(userId);
//    }
//}
