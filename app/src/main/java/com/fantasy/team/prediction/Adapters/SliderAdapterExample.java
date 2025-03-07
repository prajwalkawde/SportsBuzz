package com.fantasy.team.prediction.Adapters;

 
//import com.smarteist.autoimageslider.SliderViewAdapter;


//public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {
//
//    private Context context;
//    private List<SliderItem> mSliderItems = new ArrayList<>();
//
//    public SliderAdapterExample(Context context, List<SliderItem> mSliderItems) {
//        this.context = context;
//        this.mSliderItems = mSliderItems;
//    }
//
//    public void renewItems(List<SliderItem> sliderItems) {
//        this.mSliderItems = sliderItems;
//        notifyDataSetChanged();
//    }
//
//    public void deleteItem(int position) {
//        this.mSliderItems.remove(position);
//        notifyDataSetChanged();
//    }
//
//    public void addItem(SliderItem sliderItem) {
//        this.mSliderItems.add(sliderItem);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
//        return new SliderAdapterVH(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
//        String currentUrl = mSliderItems.get(position).getUrl();
//
//        Glide.with(viewHolder.itemView)
//                .load(IMG + mSliderItems.get(position).getBanner())
//                .into(viewHolder.imageViewBackground);
//
//        if (currentUrl != null && !currentUrl.isEmpty() && currentUrl.startsWith("https")) {
//            viewHolder.itemView.setOnClickListener(v -> {
//                Uri uri = Uri.parse(currentUrl);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                v.getContext().startActivity(intent);
//            });
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return mSliderItems.size();
//    }
//
//    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
//
//        View itemView;
//        ImageView imageViewBackground;
//        ImageView imageGifContainer;
//        TextView textViewDescription;
//
//        public SliderAdapterVH(View itemView) {
//            super(itemView);
//            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
//            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
//            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
//            this.itemView = itemView;
//        }
//    }

//}
