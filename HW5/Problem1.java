import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Problem1 {

  public static class TimeMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);

    // For storing the mapping result
    private Text word = new Text();
    
    // Processing the data
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String line = value.toString();

      // Check for timing line
      if(line.length() >= 1 && line.charAt(0)=='T'){
        word.set(value.toString().substring(13,15));
        context.write(word, one);
      }
    }
  }

  
  public static class CountReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    
    private IntWritable out = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {

      int count = 0;
      
      // Count the instances of each key
      for (IntWritable value : values) {
        count += value.get();
      }

      // Write the output to the file
      out.set(count);
      context.write(key, out);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    // Job configurations
    Job job = Job.getInstance(conf, "tweet processor");

    job.setJarByClass(Problem1.class);

    job.setMapperClass(TimeMapper.class);
    job.setCombinerClass(CountReducer.class);
    job.setReducerClass(CountReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
} 