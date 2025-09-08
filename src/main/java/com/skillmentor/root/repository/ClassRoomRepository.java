package com.skillmentor.root.repository;

import com.skillmentor.root.entity.ClassRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoomEntity, Integer> {
    List<ClassRoomEntity> findByMentorIsNull();
}