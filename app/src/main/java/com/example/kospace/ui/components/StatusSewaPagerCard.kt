package com.example.kospace.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kospace.data.model.MonthPeriodHelper
import com.example.kospace.data.model.PembayaranKos
import com.example.kospace.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatusSewaPagerCard(
    titlePrefix: String,
    payments: List<PembayaranKos>,
    modifier: Modifier = Modifier,
    onPaidMonthClick: ((PembayaranKos) -> Unit)? = null
) {
    val currentYear = MonthPeriodHelper.currentYear()
    val startMonthKey = remember(payments) {
        MonthPeriodHelper.getEarliestPaymentKey(payments)
    }
    val paidYears = remember(payments) {
        payments.mapNotNull { p ->
            p.monthPeriod.trim().split(" ").lastOrNull()?.toIntOrNull()
        }.toMutableSet().apply { add(currentYear) }.sorted()
    }
    val pagerState = rememberPagerState(
        initialPage = paidYears.indexOf(currentYear).coerceAtLeast(0),
        pageCount = { paidYears.size }
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$titlePrefix ${paidYears.getOrElse(pagerState.currentPage) { currentYear }}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                if (paidYears.size > 1) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        paidYears.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                                    .background(
                                        if (index == pagerState.currentPage) PrimaryBlue else CardBorderDivider,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            if (paidYears.size > 1) {
                Text(
                    text = "← Geser untuk lihat tahun lain →",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { pageIndex ->
                val pageYear = paidYears.getOrElse(pageIndex) { currentYear }

                Column {
                    (1..12).map { monthNum ->
                        val shortName = MonthPeriodHelper.shortMonthNames.getOrElse(monthNum - 1) { "" }
                        val paidPayment = MonthPeriodHelper.findPaidPayment(payments, monthNum, pageYear)
                        val monthKey = pageYear * 100 + monthNum
                        val isBeforeStart = monthKey < startMonthKey
                        Triple(shortName, paidPayment, isBeforeStart)
                    }.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { (shortName, paidPayment, isBeforeStart) ->
                                val isPaid = paidPayment != null
                                val bgColor = when {
                                    isPaid -> StatusGreenContainer
                                    isBeforeStart -> SurfaceLightBlue
                                    else -> StatusOrangeContainer
                                }
                                val borderColor = when {
                                    isPaid -> StatusGreen
                                    isBeforeStart -> CardBorderDivider
                                    else -> StatusOrange
                                }
                                val textColor = when {
                                    isPaid -> StatusGreen
                                    isBeforeStart -> TextSecondary
                                    else -> StatusOrange
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .background(bgColor, shape = RoundedCornerShape(10.dp))
                                        .border(
                                            width = 1.dp,
                                            color = borderColor,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .then(
                                            if (paidPayment != null && onPaidMonthClick != null) {
                                                Modifier.clickable { onPaidMonthClick(paidPayment) }
                                            } else {
                                                Modifier
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = shortName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                        when {
                                            isPaid -> {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Lunas",
                                                    tint = StatusGreen,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            !isBeforeStart -> {
                                                Text("Belum", fontSize = 9.sp, color = StatusOrange)
                                            }
                                        }
                                    }
                                }
                            }
                            repeat(4 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
