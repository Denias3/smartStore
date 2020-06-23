package mc.smartStore.handlers.tabComplete;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import mc.smartStore.SmartStore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TabChange {
    public static void tabChange(AsyncTabCompleteEvent e, List<String> comp){
        if ("/st change ".startsWith(e.getBuffer())){
            List<String> changeParam = new ArrayList<>();
            changeParam.add("n:");
            changeParam.add("id:");
            changeParam.add("mp:");
            changeParam.add("mip:");
            changeParam.add("p:");
            changeParam.add("mc:");
            changeParam.add("c:");
            changeParam.add("s:");
            if (e.getBuffer().equals("/st change ")){
                comp.addAll(changeParam);
            }
            else
                comp.add("change");
        }
        else if (e.getBuffer().startsWith("/st change ")) {
            List<String> changeParam = new ArrayList<>();
            changeParam.add("n:");
            changeParam.add("id:");
            changeParam.add("mp:");
            changeParam.add("mip:");
            changeParam.add("p:");
            changeParam.add("mc:");
            changeParam.add("c:");
            changeParam.add("s:");
            if (e.getBuffer().matches(".+\\s[a-z]{0,3}$")){

                String param = null;

                Pattern pattern1 = Pattern.compile("\\s[a-z]{0,3}$");
                Matcher matcher1 = pattern1.matcher(e.getBuffer());
                if (matcher1.find())
                    param = matcher1.group().substring(1);
                List<String> useParams = new ArrayList<>();
                Pattern pattern = Pattern.compile("\\s[a-z]+:");
                Matcher matcher = pattern.matcher(e.getBuffer());
                while (matcher.find())
                    useParams.add(matcher.group().substring(1));
                String finalParam = param;
                comp.addAll(changeParam.stream().filter(x ->{
                    for (String par :useParams){
                        if (x.equals(par))
                            return false;
                    }
                    return x.startsWith(finalParam);
                }).collect(Collectors.toList()));
            }
            else {
                if (e.getBuffer().matches(".+n:$")){
                    comp.add("n:all");
                    comp.addAll(SmartStore.stores.keySet().stream().map(x -> "n:"+x).collect(Collectors.toList()));
                }
                else if (e.getBuffer().matches(".+id:$")){
                    comp.addAll(Stream.of("all","1", "2", "3","4","5","6","7","8","9").map(x -> "id:"+x).collect(Collectors.toList()));
                }
                else {
                    for (String par: changeParam){
                        if (e.getBuffer().matches(".+"+par+"$")){
                            comp.addAll(Stream.of("1", "2", "3","4","5","6","7","8","9").map(x -> par+x).collect(Collectors.toList()));
                            break;
                        }
                    }
                }

            }

        }
    }
}
