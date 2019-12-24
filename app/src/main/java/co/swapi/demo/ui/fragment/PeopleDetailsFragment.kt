package co.swapi.demo.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import co.swapi.demo.InjectionUtils
import co.swapi.demo.R
import co.swapi.demo.model.People
import co.swapi.demo.ui.viewmodel.PeopleDetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PeopleDetailsFragment : Fragment() {

    private lateinit var viewModel: PeopleDetailsViewModel
    val args : PeopleDetailsFragmentArgs by navArgs()

    private lateinit var imageView: ImageView
    private lateinit var nameView: TextView
    private lateinit var birthYearView: TextView
    private lateinit var eyeColorView: TextView
    private lateinit var genderView: TextView
    private lateinit var hairColorView: TextView
    private lateinit var heightView: TextView
    private lateinit var massView: TextView
    private lateinit var skinColorView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_people_details, container, false)

        nameView = root.findViewById(R.id.text_name)
        imageView = root.findViewById(R.id.image)
        birthYearView = root.findViewById(R.id.text_birth_year)
        genderView = root.findViewById(R.id.text_gender)
        heightView = root.findViewById(R.id.text_height)
        massView = root.findViewById(R.id.text_mass)
        eyeColorView = root.findViewById(R.id.text_eye_color)
        hairColorView = root.findViewById(R.id.text_hair_color)
        skinColorView = root.findViewById(R.id.text_skin_color)

        // get the view model
        viewModel = ViewModelProvider(this, InjectionUtils.providePeopleDetailsViewModelFactory(root.context))
            .get(PeopleDetailsViewModel::class.java)

        Log.d("PeopleDetailsFragment", "loading people details (id: ${args.characterId})")
        viewModel.people.observe(viewLifecycleOwner, Observer<People> { bind(it) })
        viewModel.getPeople(args.characterId)

        return root
    }

    fun bind(people: People?) {
        val url = "https://picsum.photos/200/200?random=1${args.characterId}"
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.ic_account_24dp)
            .into(imageView)

        birthYearView.text = getText(people?.birthYear, getString(R.string.unknown_birth_year))
        genderView.text = getText(people?.gender, getString(R.string.unknown_gender))
        heightView.text = people?.height
        massView.text = people?.mass
        eyeColorView.text = getText(people?.eyeColor, getString(R.string.unknown_color))
        hairColorView.text = getText(people?.hairColor, getString(R.string.unknown_color))
        skinColorView.text = getText(people?.skinColor, getString(R.string.unknown_color))

        val name = people?.name?.split(' ')?.joinToString(" ") { it.capitalize() }
        nameView.text = name
        setActionBarTitle(name)
    }

    private fun getText(text:String?, defaultText: String?):String? {
        if (text !in listOf("n/a", "unknown", "none")) {
            return text?.capitalize()
        }
        return defaultText
    }

    private fun setActionBarTitle(title: String?) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}