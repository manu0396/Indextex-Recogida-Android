package com.example.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inditex.dssdkand.cell.selection.ZDSSelectionCellV2

@Composable
fun ZdsSettingsCell(
    title: String,
    subtitle: String,
    iconResource: Int,
    iconSize: Dp,
    iconTint: Color,
    minHeight: Dp = 90.dp,
    onClick: () -> Unit = {}
) {
    val titleStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        color = Color.Black,
        letterSpacing = (-0.5).sp
    )

    val descStyle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = Color.DarkGray
    )

    ZDSSelectionCellV2(
        modifier = Modifier.heightIn(min = minHeight),
        onCellClick = onClick,
        title = "",
        titleSlotContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = iconTint
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, style = titleStyle)
                    Text(text = subtitle, style = descStyle)
                }
            }
        }
    )
}
