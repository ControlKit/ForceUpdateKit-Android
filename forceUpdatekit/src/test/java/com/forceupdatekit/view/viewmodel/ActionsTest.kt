package com.forceupdatekit.view.viewmodel

import org.junit.Assert.*
import org.junit.Test

class ActionsTest {

    @Test
    fun `VIEW action should have correct value`() {
        assertEquals("VIEW", Actions.VIEW.value)
    }

    @Test
    fun `UPDATE action should have correct value`() {
        assertEquals("UPDATE", Actions.UPDATE.value)
    }

    @Test
    fun `Actions enum should have exactly two values`() {
        val actions = Actions.values()
        assertEquals(2, actions.size)
    }

    @Test
    fun `Actions enum should contain VIEW and UPDATE`() {
        val actions = Actions.values().toList()
        
        assertTrue(actions.contains(Actions.VIEW))
        assertTrue(actions.contains(Actions.UPDATE))
    }

    @Test
    fun `Actions enum values should be unique`() {
        val actions = Actions.values()
        val values = actions.map { it.value }
        
        assertEquals(values.size, values.distinct().size)
    }

    @Test
    fun `Actions enum should have correct names`() {
        assertEquals("VIEW", Actions.VIEW.name)
        assertEquals("UPDATE", Actions.UPDATE.name)
    }

    @Test
    fun `Actions enum should be comparable`() {
        assertTrue(Actions.VIEW.ordinal < Actions.UPDATE.ordinal)
    }

    @Test
    fun `Actions enum should support valueOf`() {
        assertEquals(Actions.VIEW, Actions.valueOf("VIEW"))
        assertEquals(Actions.UPDATE, Actions.valueOf("UPDATE"))
    }

    @Test
    fun `Actions enum should throw exception for invalid valueOf`() {
        try {
            Actions.valueOf("INVALID")
            fail("Should throw IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            // Expected
        }
    }

    @Test
    fun `Actions enum should support toString`() {
        assertEquals("VIEW", Actions.VIEW.toString())
        assertEquals("UPDATE", Actions.UPDATE.toString())
    }

    @Test
    fun `Actions enum should be serializable`() {
        // Test that enum can be used in collections
        val actionList = listOf(Actions.VIEW, Actions.UPDATE)
        assertEquals(2, actionList.size)
        
        val actionSet = setOf(Actions.VIEW, Actions.UPDATE)
        assertEquals(2, actionSet.size)
        
        val actionMap = mapOf(
            "view" to Actions.VIEW,
            "update" to Actions.UPDATE
        )
        assertEquals(2, actionMap.size)
    }

    @Test
    fun `Actions enum should work in when expressions`() {
        fun getActionValue(action: Actions): String {
            return when (action) {
                Actions.VIEW -> "view_action"
                Actions.UPDATE -> "update_action"
            }
        }
        
        assertEquals("view_action", getActionValue(Actions.VIEW))
        assertEquals("update_action", getActionValue(Actions.UPDATE))
    }

    @Test
    fun `Actions enum should work in switch-like expressions`() {
        fun isViewAction(action: Actions): Boolean {
            return when (action) {
                Actions.VIEW -> true
                Actions.UPDATE -> false
            }
        }
        
        assertTrue(isViewAction(Actions.VIEW))
        assertFalse(isViewAction(Actions.UPDATE))
    }
}
