package com.johnny.constanta_test

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnny.constanta_test.databinding.FilmItemBinding
import com.johnny.constanta_test.databinding.FragmentMainBinding
import com.johnny.constanta_test.model.Film
import com.johnny.constanta_test.util.PreferenceWork

class MainFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val iconDesc by lazy { ResourcesCompat.getDrawable(resources, R.drawable.ic_sort_icon_desc, null) }
    private val iconAsc by lazy { ResourcesCompat.getDrawable(resources, R.drawable.ic_sort_icon_asc, null) }
    private val filmsAdapter = FilmAdapter(mutableListOf())
     lateinit var binding: FragmentMainBinding

    companion object {
        fun getInstance() = MainFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.sort_order).icon = if (PreferenceWork.sortingDesc) iconDesc else iconAsc
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sort_order) {
            val sortingOrder = !PreferenceWork.sortingDesc
            PreferenceWork.sortingDesc = sortingOrder
            viewModel.films.value?.let { filmsAdapter.changeList(getFilmsSorted(sortingOrder, it)) }
            item.icon = if (item.icon == iconDesc) iconAsc else iconDesc
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        recyclerView = binding.recycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = filmsAdapter
        viewModel.films.observe(viewLifecycleOwner) { films ->
            progressBar.visibility = View.INVISIBLE
            filmsAdapter.changeList(getFilmsSorted(PreferenceWork.sortingDesc, films))
        }
        return binding.root
    }

    private fun getFilmsSorted(sortingDesc: Boolean, films: List<Film>): List<Film> {
        return if (sortingDesc)
            films.sortedByDescending { it.releaseYear }
        else
            films.sortedBy { it.releaseYear }
    }

    private class FilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var filmTitle: String
        private val dialogBox by lazy { AlertDialog.Builder(itemView.context) }
        private val binding: FilmItemBinding

        init {
            itemView.setOnClickListener(this)
            binding = FilmItemBinding.bind(itemView)
        }

        fun setInfo(film: Film) {
            filmTitle = film.title
            binding.filmTitle.text =
                "$filmTitle (${film.releaseYear})"
            val (name, name3, name2) = film.directorName.split(" ")

            val flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            val directorNamePlcH = "Режиссёр:"
            val spannableDirectorName =
                SpannableString("$directorNamePlcH $name2 ${name.first()}.${name3.first()}.")
            spannableDirectorName.setSpan(
                StyleSpan(Typeface.BOLD),
                directorNamePlcH.length + 1,
                spannableDirectorName.length,
                flag
            )
            binding.filmDirector.text = spannableDirectorName
            val actorsPlcH = "Актёрский состав:\n"
            val spannableActors =
                SpannableString("$actorsPlcH${film.actors.joinToString(separator = "\n") { it.actorName }}")
            spannableActors.setSpan(
                StyleSpan(Typeface.ITALIC),
                actorsPlcH.length,
                spannableActors.length,
                flag
            )
            spannableActors.setSpan(ForegroundColorSpan(Color.BLACK), 0, actorsPlcH.length, flag)
            binding.filmActors.text = spannableActors

        }

        override fun onClick(v: View?) {
            dialogBox.setTitle("Внимание")
            dialogBox.setMessage("Фильм \"$filmTitle\" был нажат")
            dialogBox.setPositiveButton("OK", null)
            dialogBox.show()
        }
    }

    private class FilmAdapter(val films: MutableList<Film>) : RecyclerView.Adapter<FilmHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmHolder {
            return FilmHolder(
                FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
            )
        }

        override fun onBindViewHolder(holder: FilmHolder, position: Int) {
            holder.setInfo(films[position])
        }

        override fun getItemCount(): Int = films.size

        fun changeList(newList: List<Film>) {
            films.clear()
            films.addAll(newList)
            notifyDataSetChanged()
        }
    }
}