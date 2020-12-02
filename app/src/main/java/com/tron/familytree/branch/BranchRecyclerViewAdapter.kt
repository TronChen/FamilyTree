//package com.tron.familytree.branch
//
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.tron.familytree.R
//import com.tron.familytree.data.User
//import com.tron.familytree.databinding.ItemListBranchUserBinding
//import com.tron.familytree.databinding.ItemListEmptyBinding
//import com.tron.familytree.databinding.ItemListQueryDownBinding
//import com.tron.familytree.databinding.ItemListQueryUpBinding
//
//
//
//class BranchRecyclerViewAdapter(private val itemClickListener: UserOnItemClickListener)
//    : ListAdapter<User,RecyclerView.ViewHolder>(UserDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when(viewType){
//            ITEM_VIEW_TYPE_USER -> {UserViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_EMPTY -> {EmptyViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_HORIZONTAL -> {HorStrViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_RIGHT_HOR -> {RightHorViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_RIGHT_DOWN -> {RightDownViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_LEFT_HOR -> {LeftHorViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_LEFT_DOWN -> {LeftDownViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_TO_TOP -> {ToTopViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_TO_DOWN -> {ToDownViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_QUERY_UP -> {QueryUpViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_QUERY_DOWN -> {QueryDownViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_VERTICAL -> {VerticalViewHolder.from(parent)}
//            ITEM_VIEW_TYPE_CROSS -> {CrossViewHolder.from(parent)}
//            else -> throw ClassCastException("Unknown ViewType: $viewType")
//        }
//    }
//
////    override fun getItemCount(): Int {
////        return 64
////    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = getItem(position)
//        when(holder){
//            is UserViewHolder ->{
//                val user = item as User
//                holder.bind(user)
//                holder.itemView.setOnClickListener{
//                    itemClickListener.onItemClicked(item)
//                }
//            }
//            is EmptyViewHolder ->{
//                val user = item as User
//                holder.bind(user)
//                holder.itemView.setOnClickListener{
//                    itemClickListener.onItemClicked(item)
//                }
//            }
//            is QueryUpViewHolder ->{
//                val user = item as User
//                holder.bind(user)
//                holder.itemView.setOnClickListener{
//                    itemClickListener.onItemClicked(item)
//                }
//            }
//            is QueryDownViewHolder ->{
//                val user = item as User
//                holder.bind(user)
//                holder.itemView.setOnClickListener{
//                    itemClickListener.onItemClicked(item)
//                }
//            }
//    }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        val lv1 = 0
//        val lv2 = 7
//        val lv3 = 14
//        val lv4 = 21
//        val lv5 = 28
//        val lv6 = 35
//        val lv7 = 42
//        val lv8 = 49
//        val lv9 = 56
//
//        //User
//        val lv2_1 = listOf(lv2 + 3)
//        val lv2_2 = listOf(lv2 + 1, lv2 + 5)
//        val lv2_3 = listOf(lv2 + 1, lv2 + 3, lv2 + 5)
//        val lv2_4 = listOf(lv2, lv2 + 2, lv2 + 4, lv2 + 6)
//        val lv2_5 = listOf(lv2, lv2 + 2, lv2 + 3, lv2 + 4, lv2 + 6)
//        val lv2_6 = listOf(lv2, lv2 + 1, lv2 + 2, lv2 + 4, lv2 + 5, lv2 + 6)
//        val lv2_7 = listOf(lv2, lv2 + 1, lv2 + 2, lv2 + 3, lv2 + 4, lv2 + 5, lv2 + 6)
//
//        val lv5_1 = listOf(lv5 + 3)
//        val lv5_2 = listOf(lv5 + 1, lv5 + 5)
//        val lv5_3 = listOf(lv5 + 1, lv5 + 3, lv5 + 5)
//        val lv5_4 = listOf(lv5, lv5 + 2, lv5 + 4, lv5 + 6)
//        val lv5_5 = listOf(lv5, lv5 + 2, lv5 + 3, lv5 + 4, lv5 + 6)
//        val lv5_6 = listOf(lv5, lv5 + 1, lv5 + 2, lv5 + 4, lv5 + 5, lv5 + 6)
//        val lv5_7 = listOf(lv5, lv5 + 1, lv5 + 2, lv5 + 3, lv5 + 4, lv5 + 5, lv5 + 6)
//
//        val lv8_1 = listOf(lv8 + 3)
//        val lv8_2 = listOf(lv8 + 1, lv8 + 5)
//        val lv8_3 = listOf(lv8 + 1, lv8 + 3, lv8 + 5)
//        val lv8_4 = listOf(lv8, lv8 + 2, lv8 + 4, lv8 + 6)
//        val lv8_5 = listOf(lv8, lv8 + 2, lv8 + 3, lv8 + 4, lv8 + 6)
//        val lv8_6 = listOf(lv8, lv8 + 1, lv8 + 2, lv8 + 4, lv8 + 5, lv8 + 6)
//        val lv8_7 = listOf(lv8, lv8 + 1, lv8 + 2, lv8 + 3, lv8 + 4, lv8 + 5, lv8 + 6)
//
//        //Query lv1
//        val QU_1 = listOf(lv1 + 3)
//        val QU_2 = listOf(lv1 + 1, lv1 + 5)
//        val QU_3 = listOf(lv1 + 1, lv1 + 3, lv1 + 5)
//        val QU_4 = listOf(lv1, lv1 + 2, lv1 + 4, lv1 + 6)
//        val QU_5 = listOf(lv1, lv1 + 2, lv1 + 3, lv1 + 4, lv1 + 6)
//        val QU_6 = listOf(lv1, lv1 + 1, lv1 + 2, lv1 + 4, lv1 + 5, lv1 + 6)
//        val QU_7 = listOf(lv1, lv1 + 1, lv1 + 2, lv1 + 3, lv1 + 4, lv1 + 5, lv1 + 6)
//
//        //QueryDown
//        val QD_1 = listOf(lv9 + 3)
//        val QD_2 = listOf(lv9 + 1, lv9 + 5)
//        val QD_3 = listOf(lv9 + 1, lv9 + 3, lv9 + 5)
//        val QD_4 = listOf(lv9, lv9 + 2, lv9 + 4, lv9 + 6)
//        val QD_5 = listOf(lv9, lv9 + 2, lv9 + 3, lv9 + 4, lv9 + 6)
//        val QD_6 = listOf(lv9, lv9 + 1, lv9 + 2, lv9 + 4, lv9 + 5, lv9 + 6)
//        val QD_7 = listOf(lv9, lv9 + 1, lv9 + 2, lv9 + 3, lv9 + 4, lv9 + 5, lv9 + 6)
//
//        //Pipe lv3
//        val RD_lv3 = listOf(lv3,lv3+4)
//        val LD_lv3 = listOf(lv3+2,lv3+6)
//        val DD_lv3 = listOf(lv3+1,lv3+5)
//
//        //Pipe lv4
//        val Vert_lv4 = listOf(lv4+1,lv4+5)
//
//        //Pipe lv6
//        val RD_lv6 = listOf(lv6+1)
//        val LD4_lv6 = listOf(lv6+5)
//        val DD4_lv6 = listOf(lv6+3)
//        val Hor_lv6 = listOf(lv6+2,lv6+4)
//
//        when(position){
//            in QU_4 -> return ITEM_VIEW_TYPE_QUERY_UP
//
//            in RD_lv3 -> return ITEM_VIEW_TYPE_RIGHT_DOWN
//            in LD_lv3 -> return ITEM_VIEW_TYPE_LEFT_DOWN
//            in DD_lv3 -> return ITEM_VIEW_TYPE_TO_DOWN
//            in Vert_lv4 -> return ITEM_VIEW_TYPE_VERTICAL
//            in RD_lv6 -> return ITEM_VIEW_TYPE_RIGHT_DOWN
//            in LD4_lv6 -> return ITEM_VIEW_TYPE_LEFT_DOWN
//            in DD4_lv6 -> return ITEM_VIEW_TYPE_TO_DOWN
//            in Hor_lv6 -> return ITEM_VIEW_TYPE_HORIZONTAL
//
//            in lv2_4 -> return ITEM_VIEW_TYPE_USER
//            in lv5_2 -> return ITEM_VIEW_TYPE_USER
//
//        }
//
//
////        //Pipe lv7_1
////        val Vert_lv7_1 = listOf(lv7+3)
////
////        when(position){
////            in lv8_1 -> return ITEM_VIEW_TYPE_USER
////            in Vert_lv7_1 -> return ITEM_VIEW_TYPE_VERTICAL
////            in QD_1 -> return ITEM_VIEW_TYPE_QUERY_DOWN
////        }
////
////
////        //Pipe lv7_2
////        val Hor_lv7_2 = listOf(lv7+2,lv7+4)
////        val DU_lv7_2 = listOf(lv7+3)
////        val RU_lv7_2 = listOf(lv7+1)
////        val LU_lv7_2 = listOf(lv7+5)
////
////        when(position){
////            in Hor_lv7_2 -> return ITEM_VIEW_TYPE_HORIZONTAL
////            in DU_lv7_2 -> return ITEM_VIEW_TYPE_TO_TOP
////            in RU_lv7_2 -> return ITEM_VIEW_TYPE_RIGHT_HOR
////            in LU_lv7_2 -> return ITEM_VIEW_TYPE_LEFT_HOR
////
////            in lv8_2 -> return ITEM_VIEW_TYPE_USER
////            in QD_2 -> return ITEM_VIEW_TYPE_QUERY_DOWN
////        }
////
////
////        //Pipe lv7_3
////        val Hor_lv7_3 = listOf(lv7+2,lv7+4)
////        val DU_lv7_3 = listOf(lv7+3)
////        val RU_lv7_3 = listOf(lv7+1)
////        val LU_lv7_3 = listOf(lv7+5)
////        val Cross_lv7_3 = listOf(lv7+3)
////
////        when(position){
////            in Hor_lv7_3 -> return ITEM_VIEW_TYPE_HORIZONTAL
////            in DU_lv7_3 -> return ITEM_VIEW_TYPE_TO_TOP
////            in RU_lv7_3 -> return ITEM_VIEW_TYPE_RIGHT_HOR
////            in LU_lv7_3 -> return ITEM_VIEW_TYPE_LEFT_HOR
////            in Cross_lv7_3 -> return ITEM_VIEW_TYPE_CROSS
////
////            in lv8_3 -> return ITEM_VIEW_TYPE_USER
////            in QD_3 -> return ITEM_VIEW_TYPE_QUERY_DOWN
////        }
////
////
////        //Pipe lv7_4
////        val DD_lv7_4 = listOf(lv7+2,lv7+4)
////        val Hor_lv7_4 = listOf(lv7+1,lv7+5)
////        val DU_lv7_4 = listOf(lv7+3)
////        val RU_lv7_4 = listOf(lv7)
////        val LU_lv7_4 = listOf(lv7+6)
////
////        when(position){
////            in DD_lv7_4 -> return ITEM_VIEW_TYPE_TO_DOWN
////            in Hor_lv7_4 -> return ITEM_VIEW_TYPE_HORIZONTAL
////            in DU_lv7_4 -> return ITEM_VIEW_TYPE_TO_TOP
////            in RU_lv7_4 -> return ITEM_VIEW_TYPE_RIGHT_HOR
////            in LU_lv7_4 -> return ITEM_VIEW_TYPE_LEFT_HOR
////
////            in lv8_4 -> return ITEM_VIEW_TYPE_USER
////            in QD_4 -> return ITEM_VIEW_TYPE_QUERY_DOWN
////        }
//
//
////        //Pipe lv7_5
////        val DD_lv7_5 = listOf(lv7+2,lv7+4)
////        val Hor_lv7_5 = listOf(lv7+1,lv7+5)
////        val RU_lv7_5 = listOf(lv7)
////        val LU_lv7_5 = listOf(lv7+6)
////        val Cross_lv7_5 = listOf(lv7+3)
////
////        when(position){
////            in DD_lv7_5 -> return ITEM_VIEW_TYPE_TO_DOWN
////            in Hor_lv7_5 -> return ITEM_VIEW_TYPE_HORIZONTAL
////            in RU_lv7_5 -> return ITEM_VIEW_TYPE_RIGHT_HOR
////            in LU_lv7_5 -> return ITEM_VIEW_TYPE_LEFT_HOR
////            in Cross_lv7_5 -> return ITEM_VIEW_TYPE_CROSS
////
////            in lv8_5 -> return ITEM_VIEW_TYPE_USER
////            in QD_5 -> return ITEM_VIEW_TYPE_QUERY_DOWN
////        }
//
//
//        //Pipe lv7_6
//        val DD_lv7_6 = listOf(lv7+1,lv7+2,lv7+4,lv7+5)
//        val Hor_lv7_6 = listOf(lv7+1,lv7+5)
//        val RU_lv7_6 = listOf(lv7)
//        val LU_lv7_6 = listOf(lv7+6)
//        val DU_lv7_6 = listOf(lv7+3)
//
//        when(position){
//            in DD_lv7_6 -> return ITEM_VIEW_TYPE_TO_DOWN
//            in Hor_lv7_6 -> return ITEM_VIEW_TYPE_HORIZONTAL
//            in DU_lv7_6 -> return ITEM_VIEW_TYPE_TO_TOP
//            in RU_lv7_6 -> return ITEM_VIEW_TYPE_RIGHT_HOR
//            in LU_lv7_6 -> return ITEM_VIEW_TYPE_LEFT_HOR
//
//            in lv8_6 -> return ITEM_VIEW_TYPE_USER
//            in QD_6 -> return ITEM_VIEW_TYPE_QUERY_DOWN
//        }
//
//
//        return ITEM_VIEW_TYPE_EMPTY
//    }
//
//
//    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
//        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//
//    class UserViewHolder(val binding:ItemListBranchUserBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(user: User) {
//            Log.e("UserViewHolder","$user")
//            binding.user = user
//            val aaa = mutableListOf<User>()
//            aaa.add(user)
//            Log.e("aaa", aaa.toString())
//            // This is important, because it forces the data binding to execute immediately,
//            // which allows the RecyclerView to make the correct view size measurements
//            binding.executePendingBindings()
//        }
//        companion object {
//            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemListBranchUserBinding.inflate(layoutInflater, parent, false)
//                return UserViewHolder(binding)
//            }
//        }
//    }
//
//    class EmptyViewHolder(val binding:ItemListEmptyBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(user: User) {
//            Log.e("EmptyViewHolder","$user")
//            binding.user = user
//
//            // This is important, because it forces the data binding to execute immediately,
//            // which allows the RecyclerView to make the correct view size measurements
//            binding.executePendingBindings()
//        }
//        companion object {
//            fun from(parent: ViewGroup): EmptyViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemListEmptyBinding.inflate(layoutInflater, parent, false)
//                return EmptyViewHolder(binding)
//            }
//        }
//    }
//
//    class QueryUpViewHolder(val binding:ItemListQueryUpBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(user: User) {
//            Log.e("QueryUpViewHolder","$user")
//            binding.user = user
//
//            // This is important, because it forces the data binding to execute immediately,
//            // which allows the RecyclerView to make the correct view size measurements
//            binding.executePendingBindings()
//        }
//        companion object {
//            fun from(parent: ViewGroup): QueryUpViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemListQueryUpBinding.inflate(layoutInflater, parent, false)
//                return QueryUpViewHolder(binding)
//            }
//        }
//    }
//
//    class QueryDownViewHolder(val binding:ItemListQueryDownBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(user: User) {
//            Log.e("QueryDownViewHolder","$user")
//            binding.user = user
//
//            // This is important, because it forces the data binding to execute immediately,
//            // which allows the RecyclerView to make the correct view size measurements
//            binding.executePendingBindings()
//        }
//        companion object {
//            fun from(parent: ViewGroup): QueryDownViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemListQueryDownBinding.inflate(layoutInflater, parent, false)
//                return QueryDownViewHolder(binding)
//            }
//        }
//    }
//
//    class HorStrViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): HorStrViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_branch_horizontal_straight, parent, false)
//                return HorStrViewHolder(view)
//            }
//        }
//    }
//
//    class RightHorViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): RightHorViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_branch_right_hor, parent, false)
//                return RightHorViewHolder(view)
//            }
//        }
//    }
//
//    class RightDownViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): RightDownViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_right_down, parent, false)
//                return RightDownViewHolder(view)
//            }
//        }
//    }
//
//    class LeftHorViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): LeftHorViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_branch_left_hor, parent, false)
//                return LeftHorViewHolder(view)
//            }
//        }
//    }
//
//    class LeftDownViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): LeftDownViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_left_down, parent, false)
//                return LeftDownViewHolder(view)
//            }
//        }
//    }
//
//    class ToTopViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): ToTopViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_to_top, parent, false)
//                return ToTopViewHolder(view)
//            }
//        }
//    }
//
//    class ToDownViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): ToDownViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_to_down, parent, false)
//                return ToDownViewHolder(view)
//            }
//        }
//    }
//
//    class VerticalViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): VerticalViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_vertical, parent, false)
//                return VerticalViewHolder(view)
//            }
//        }
//    }
//
//
//
//    class CrossViewHolder(view: View): RecyclerView.ViewHolder(view) {
//        companion object {
//            fun from(parent: ViewGroup): CrossViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val view = layoutInflater.inflate(R.layout.item_list_cross, parent, false)
//                return CrossViewHolder(view)
//            }
//        }
//    }
//
//    class UserOnItemClickListener(val clickListener: (user: User) -> Unit ){
//        fun onItemClicked(user: User) = clickListener(user)
//    }
//
//    companion object {
//        const val ITEM_VIEW_TYPE_USER = 0
//        const val ITEM_VIEW_TYPE_EMPTY = 1
//        const val ITEM_VIEW_TYPE_HORIZONTAL = 2
//        const val ITEM_VIEW_TYPE_RIGHT_HOR = 3
//        const val ITEM_VIEW_TYPE_RIGHT_DOWN = 4
//        const val ITEM_VIEW_TYPE_LEFT_HOR = 5
//        const val ITEM_VIEW_TYPE_LEFT_DOWN = 6
//        const val ITEM_VIEW_TYPE_TO_TOP = 7
//        const val ITEM_VIEW_TYPE_TO_DOWN = 8
//        const val ITEM_VIEW_TYPE_QUERY_UP = 9
//        const val ITEM_VIEW_TYPE_QUERY_DOWN = 10
//        const val ITEM_VIEW_TYPE_VERTICAL = 11
//        const val ITEM_VIEW_TYPE_CROSS = 12
//    }
//}
//
////sealed class DataItem {
////    data class FullProduct(val user: User): DataItem() {
////        override val id = user.id.toInt()
////    }
////
////    data class CollageProduct(val user: User): DataItem() {
////        override val id = user.id.toInt()
////    }
////
////    data class Header(val title: String): DataItem() {
////        override val id = Long.MIN_VALUE.toInt()
////    }
////    abstract val id: Int
////}