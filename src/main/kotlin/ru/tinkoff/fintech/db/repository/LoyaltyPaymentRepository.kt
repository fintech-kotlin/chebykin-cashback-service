package ru.tinkoff.fintech.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import java.time.LocalDateTime

@Repository
interface LoyaltyPaymentRepository : JpaRepository<LoyaltyPaymentEntity, Long> {
    suspend fun findByCardIdAndSignAndDateTimeAfter(cardId: String, sign: String, dateTime: LocalDateTime) : Collection<LoyaltyPaymentEntity>
}
