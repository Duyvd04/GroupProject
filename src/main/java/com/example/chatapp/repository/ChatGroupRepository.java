//package com.example.chatapp.repository;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {}
//
//@Repository
//public interface ChatGroupMemberRepository extends JpaRepository<ChatGroupMember, Long> {
//
//    @Query("SELECT g FROM ChatGroup g JOIN ChatGroupMember m ON g.id = m.groupId WHERE m.userId = :userId")
//    List<ChatGroup> findGroupsByUserId(Long userId);
//}
