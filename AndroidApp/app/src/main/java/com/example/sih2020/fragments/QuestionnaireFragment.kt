package com.example.sih2020.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sih2020.R
import com.example.sih2020.adapters.QuestionnaireAdapter
import com.example.sih2020.classes.Questionnaire

import com.example.sih2020.utils.BaseFragment

import com.example.sih2020.dbClasses.Records
import com.example.sih2020.dbClasses.model.pendingsurvey.PendingSurveyDatabase
import com.example.sih2020.dbClasses.model.pendingsurvey.PendingSurveyEntity


import com.example.sih2020.utils.Constants
import com.example.sih2020.utils.onQuestionclicked
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class QuestionnaireFragment : BaseFragment(), onQuestionclicked {


    private var submitButton: FloatingActionButton? = null
    private  var recyclerView: RecyclerView? = null
    private  var adapter:QuestionnaireAdapter?= null
    private var linearLayoutManager: LinearLayoutManager?= null
    private  var card: CardView?=null
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        submitButton = view.findViewById<FloatingActionButton>(R.id.submitButton)
        view.findViewById<FloatingActionButton>(R.id.submitButton).setOnClickListener(View.OnClickListener {
            if(Constants.qList.size == Questionnaire.questionnaireQuestions.size){
                UpdateToRoom()
            }else{
                Toast.makeText(requireContext(),"Submit all questions",Toast.LENGTH_SHORT).show()
            }
        })

        adapter = QuestionnaireAdapter(this)
        recyclerView=view.findViewById(R.id.recyclerView)
        linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = adapter

    }

    private fun UpdateToRoom() {
        /*
        *TODO : Save the details from Constants to room
        */

        val record: Records= Records()
        record.creationDate = Constants.creationDate
        record.gpsLocation = Constants.gpsSnap
        record.officerId = Constants.officerId
        record.overallReview = Constants.overallReview
        record.questions = Constants.qList
        Constants.mapList.add(mapOf(Pair(Constants.schoolId,record)))

        launch {
            context?.let {
                if(PendingSurveyDatabase(it).getPendingSurveyDao().getAllList().isEmpty())
                {
                    val list = PendingSurveyEntity(Constants.schoolId,record)
                    PendingSurveyDatabase(it).getPendingSurveyDao().addList(list)
                    Toast.makeText(requireContext(), "saved to room", Toast.LENGTH_SHORT).show()
                }
                else{
                    /**TODO
                     * Update the else part as required to append or update any existing record
                     */
                }

            }
        }






    }

    override fun Onclicked(question: String, view: View,position: Int) {
            when(view.id)
            {
               R.id.cardviewQuestion-> {
                   val bundle = bundleOf("QuestionNumber" to position)
                   if(Constants.questionsCount == position){
                   navController!!.navigate(R.id.QuestionListToAnswer,bundle)
                   //
                   }else{
                       Toast.makeText(requireContext(),"Submit Question ${Constants.questionsCount+1} first ",Toast.LENGTH_LONG).show()
                   }
               }

                R.id.submitButton-> {
                    if(Constants.questionsCount == Constants.qList.size){

                    }else{
                        Toast.makeText(requireContext(),"Submit All Questions", Toast.LENGTH_SHORT).show()
                    }
                }
//                {
//                   val action= QuestionnaireFragmentDirections.QuestionListToAnswer(question)
//                    view.findNavController().navigate(action)
//                }
            }

    }


}