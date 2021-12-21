package com.grootan.technologies.Adapter

import com.grootan.technologies.Module.CubeModel
import androidx.recyclerview.widget.RecyclerView
import com.grootan.technologies.Adapter.MatrixAdapter.MyViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.grootan.technologies.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.grootan.technologies.Activity.HomeScreenActivity
import android.widget.LinearLayout
import java.util.*

class MatrixAdapter(var mContext: Context, var cubeModelList: List<CubeModel?>) :
    RecyclerView.Adapter<MyViewHolder>() {
    var flag = 0
    var firstPosition = -1
    var positionToSwap = -1
    private var checkSwap = false
    var noOfMoves = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_cube_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val cubeModel = cubeModelList[position]
        holder.lin_cube.setBackgroundColor(Color.parseColor(cubeModel!!.color))
        if (position == firstPosition) {
            holder.img_cube.visibility = View.VISIBLE
        } else {
            holder.img_cube.visibility = View.GONE
        }
        holder.lin_cube.setOnClickListener {
            if (checkSwap) {
                if (flag % 2 == 0) {
                    if (!cubeModel.isAddRemove) {
                        holder.img_cube.visibility = View.VISIBLE
                        cubeModel.isAddRemove = true
                    } else {
                        cubeModel.isAddRemove = false
                        holder.img_cube.visibility = View.GONE
                    }
                    firstPosition = holder.adapterPosition
                } else {
                    positionToSwap = holder.adapterPosition
                    if (firstPosition > 0 && positionToSwap > 0) {
                        Collections.swap(cubeModelList, firstPosition, positionToSwap)
                    }
                    notifyItemMoved(firstPosition, positionToSwap)
                    noOfMoves++
                    (mContext as HomeScreenActivity).getNoOfMoves(noOfMoves)
                    firstPosition = -1
                    positionToSwap = -1
                }
                flag++
            }
        }
    }

    fun setCheckSwap(swap: Boolean) {
        checkSwap = swap
    }

    override fun getItemCount(): Int {
        return cubeModelList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var img_cube: ImageView
        var lin_cube: LinearLayout

        init {
            lin_cube = view.findViewById(R.id.lin_cube)
            img_cube = view.findViewById(R.id.img_cube)
        }
    }
}