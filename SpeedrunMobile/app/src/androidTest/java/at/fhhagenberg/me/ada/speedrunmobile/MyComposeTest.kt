package at.fhhagenberg.me.ada.speedrunmobile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.fhhagenberg.me.ada.speedrunmobile.core.Category
import at.fhhagenberg.me.ada.speedrunmobile.core.UNDEFINED_CATEGORY
import at.fhhagenberg.me.ada.speedrunmobile.screens.CategoryNavigationBar
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.SpeedrunMobileTheme
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule


class MyComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCategories: List<Category> = listOf(
        Category(id = "02qr00pk",
            name = "Any%",
            rules = "Any% - Reach the credits of the game. \\r\\n\\r\\n* The run stops on credits, not the final cutscene. You must skip the final cutscene before getting your final time. \\r\\n* If you use the save \\u0026 quit mechanic at any point during a run, any delay in loading your character will cause a run to get rejected.\\r\\n\\r\\n##Chinese Translation \\u4e2d\\u6587\\u7ffb\\u8bd1\\r\\nhttps://www.bilibili.com/read/cv15826478"),
        Category(id = "9kvnee8d",
            name = "Any% Unrestricted",
            rules = "Any% Unrestricted - Reach the credits of the game. \\r\\n\\r\\n* The run stops on credits, not the final cutscene. You must skip the final cutscene before getting your final time. \\r\\n* Force quitting the game via Alt+F4 is allowed for executing wrong warps. \\r\\n* Zips are allowed. \\r\\n* If you use the save \\u0026 quit mechanic at any point during a run, any delay in loading your character will cause a run to get rejected.\\r\\n\\r\\n##Chinese Translation \\u4e2d\\u6587\\u7ffb\\u8bd1\\r\\nhttps://www.bilibili.com/read/cv15826478"),
        Category(id = "mkey0882",
            name = "Any% No Wrong Warp",
            rules = "Any% No Wrong Warp - Reach the credits of the game. The use of the wrong warp and \\u0022Pegasus\\u0022 glitches are banned.\\r\\n\\r\\n* The run stops on credits, not the final cutscene. You must skip the final cutscene before getting your final time.\\r\\n* If you use the save \\u0026 quit mechanic at any point during a run, any delay in loading your character will cause a run to get rejected.\\r\\n\\r\\n##Chinese Translation \\u4e2d\\u6587\\u7ffb\\u8bd1\\r\\nhttps://www.bilibili.com/read/cv15826478")
    )

    @Test
    fun myTest() {
        composeTestRule.setContent {
            SpeedrunMobileTheme {
                CategoryNavigationBar(currentCategoryID = UNDEFINED_CATEGORY, categories = testCategories, openDrawer = {})
            }
        }

        composeTestRule.onNodeWithText("Any%").assertIsDisplayed()
    }
}