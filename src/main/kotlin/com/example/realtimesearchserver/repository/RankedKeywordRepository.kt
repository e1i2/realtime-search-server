package com.example.realtimesearchserver.repository

import com.example.realtimesearchserver.entity.RankedKeywordEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RankedKeywordRepository: CoroutineCrudRepository<RankedKeywordEntity, Long>
