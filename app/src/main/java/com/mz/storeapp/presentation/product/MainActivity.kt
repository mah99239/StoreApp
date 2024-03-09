package com.mz.storeapp.presentation.product

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mz.storeapp.R
import com.mz.storeapp.databinding.ActivityMainBinding
import com.mz.storeapp.presentation.utils.setupSnackbar
import com.mz.storeapp.presentation.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity of the application.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        binding.lifecycleOwner = this
        observerNetwork()
        observerSnackbarMessage()
    }

    /**
     * Sets up the navigation controller.
     */
    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            // Update the activity's title based on the fragment that the user is navigating to
            title = destination.label
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Observes the network state and updates the UI accordingly.
     */
    private fun observerNetwork() {
        viewModel.isOnline.observe(this) {
            binding.isAvailable = it
        }
    }

    /**
     * Observes the snackbar message and shows it when necessary.
     */
    private fun observerSnackbarMessage() {
        binding.root.setupSnackbar(
            this,
            viewModel.userMessage,
            Snackbar.LENGTH_LONG,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}