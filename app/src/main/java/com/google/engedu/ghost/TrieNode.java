/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Random rand = new Random();


    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<Character,TrieNode>temp=children;
        for(int i=0;i<s.length();i++)
        {
            if(!temp.containsKey(s.charAt(i))){
                temp.put(s.charAt(i),new TrieNode());
            }
            if(i==s.length()-1){
                temp.get(s.charAt(i)).isWord=true;
            }
            temp=temp.get(s.charAt(i)).children;
        }
    }

    public boolean isWord(String s) {
      TrieNode temp=searchNode(s);
      if(temp==null)
        return false;
      else
          return temp.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode temp=searchNode(s);
        if (temp==null)
        return null;
        while (!temp.isWord)
        {
            for(Character ch:temp.children.keySet()){
                temp=temp.children.get(ch);
                s+=ch;
                break;
            }
        }
        return s;
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode temp = searchNode(s);
        if (temp == null){
            return null;
        }
        // get a random word
        ArrayList<Character> charsNoWord = new ArrayList<>();
        ArrayList<Character> charsWord = new ArrayList<>();
        Character c;

        while (true){
            charsNoWord.clear();
            charsWord.clear();
            for (Character ch: temp.children.keySet()){
                if (temp.children.get(ch).isWord){
                    charsWord.add(ch);
                } else {
                    charsNoWord.add(ch);
                }
            }
            if (charsNoWord.size() == 0){
                s += charsWord.get( rand.nextInt(charsWord.size()) );
                break;
            } else {
                c = charsNoWord.get( rand.nextInt(charsNoWord.size()) );
                s += c;
                temp = temp.children.get(c);
            }
        }
        return s;

    }
    private TrieNode searchNode(String s)
    {
       TrieNode temp=this;
       for(int i=0;i<s.length();i++)
       {
           if(!temp.children.containsKey(s.charAt(i)))
           {
               return null;
           }
           temp=temp.children.get(s.charAt(i));
       }
       return temp;
    }

}
