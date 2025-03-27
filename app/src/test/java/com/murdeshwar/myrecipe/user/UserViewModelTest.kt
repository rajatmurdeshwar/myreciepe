package com.murdeshwar.myrecipe.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import com.murdeshwar.myrecipe.FakeRecipeRepository
import com.murdeshwar.myrecipe.MainCoroutineRule
import com.murdeshwar.myrecipe.data.source.LoginUser
import com.murdeshwar.myrecipe.data.source.User
import com.murdeshwar.myrecipe.ui.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class UserViewModelTest {

    // Subject under test
    private lateinit var viewModel: UserViewModel

    // Fake repository and DataStore
    private lateinit var fakeRepository: FakeRecipeRepository
    private lateinit var testDataStore: DataStore<Preferences>

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        fakeRepository = FakeRecipeRepository()
        testDataStore = createTestDataStore()
        viewModel = UserViewModel(fakeRepository, testDataStore)
    }

    @Test
    fun `signup should emit success when repository returns a token`() = runTest {
        // Given: Fake repository returns a token
        val user = User(name = "testUser", phonenumber = "712637332", city = "Dublin", email = "test@example.com", password = "test123")
        //fakeRepository.signupToken = "fake_token"

        // When: User signs up
        viewModel.signup(user)

        // Then: UI state should be success
        assertThat(viewModel.uiState.first()).isInstanceOf(UserViewModel.UIEvent.Success::class.java)
    }

    @Test
    fun `signup should emit error when repository throws an exception`() = runTest {
        // Given: Fake repository throws an exception
        val user = User(name = "testUser", phonenumber = "712637332", city = "Dublin", email = "test@example.com", password = "test123")
        fakeRepository.setShouldThrowError(true)

        // When: User signs up
        viewModel.signup(user)

        // Then: UI state should be error
        assertThat(viewModel.uiState.first()).isInstanceOf(UserViewModel.UIEvent.Error::class.java)
    }

    @Test
    fun `login should emit success when repository returns a token`() = runTest {
        // Given: Fake repository returns a token
        val loginUser = LoginUser("test@example.com", "password")

        // When: User logs in
        viewModel.login(loginUser)

        // Then: UI state should be success
        assertThat(viewModel.uiState.first()).isInstanceOf(UserViewModel.UIEvent.Success::class.java)
    }

    @Test
    fun `login should emit error when repository throws an exception`() = runTest {
        // Given: Fake repository throws an exception
        val loginUser = LoginUser("test@example.com", "password")
        fakeRepository.setShouldThrowError(true)

        // When: User logs in
        viewModel.login(loginUser)

        // Then: UI state should be error
        assertThat(viewModel.uiState.first()).isInstanceOf(UserViewModel.UIEvent.Error::class.java)
    }

    @Test
    fun `saveLoginState should persist login state in DataStore`() = runTest {
        // When: Saving login state
        viewModel.saveLoginState(true)

        // Then: Read login state should return true
        val isLoggedIn = viewModel.readLoginState().first()
        assertThat(isLoggedIn).isTrue()
    }

    @Test
    fun `resetUIState should reset state to Idle`() = runTest {
        // Given: UI state is success
        viewModel.signup(User(name = "testUser", phonenumber = "712637332", city = "Dublin", email = "test@example.com", password = "test123"))

        // When: Reset UI state
        viewModel.resetUIState()

        // Then: UI state should be Idle
        assertThat(viewModel.uiState.first()).isInstanceOf(UserViewModel.UIEvent.Idle::class.java)
    }

    // Helper function to create a test DataStore
    private fun createTestDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { File("test.preferences_pb") }
        )
    }
}
