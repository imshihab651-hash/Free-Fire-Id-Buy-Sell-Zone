package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppRepository
import com.example.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FF ID Zone", appName)
  }

  @Test
  fun `verify 10 deal slots configured`() {
    val slots = AppRepository.dealSlots.value
    assertEquals(10, slots.size)
    assertEquals(1000, slots[0].minAmount)
    assertEquals(150, slots[0].adminFee)
    assertEquals(10000, slots[9].minAmount)
    assertEquals(1000, slots[9].adminFee)
  }

  @Test
  fun `public registration prevents admin or moderator escalation`() {
    val adminAttempt = AppRepository.register(
      name = "Hacker",
      username = "hacker_admin",
      pass = "123456",
      phone = "01700-000000",
      selectedRole = Role.ADMIN
    )
    assertTrue(adminAttempt.isFailure)

    val modAttempt = AppRepository.register(
      name = "Hacker",
      username = "hacker_mod",
      pass = "123456",
      phone = "01700-000000",
      selectedRole = Role.MODERATOR
    )
    assertTrue(modAttempt.isFailure)
  }

  @Test
  fun `verify owner identity is Shihab Talukder`() {
    val owner = AppRepository.ownerUser
    assertEquals("Shihab Talukder", owner.name)
    assertEquals(Role.OWNER, owner.role)
    assertTrue(owner.isOwner)
  }
}
