package me.kingingo.kcore.kListen;
import java.util.ArrayList;

public class kArrayList<ValueType>
{
  private ArrayList<ValueType> _wrappedArrayList = new ArrayList<>();

  public Object[] toList(){
	  Object[] v = new Object[_wrappedArrayList.size()];
	  int i=0;
	  for(ValueType type : _wrappedArrayList){
		  v[i]=type;
		  i++;
	  }
	  return v;
  }
  
  public boolean contains(ValueType v)
  {
    return this._wrappedArrayList.contains(v);
  }

  public boolean remove(ValueType v)
  {
    return this._wrappedArrayList.remove(v);
  }

  public ValueType get(int i)
  {
    return this._wrappedArrayList.get(i);
  }

  public ValueType remove(int i)
  {
    return this._wrappedArrayList.remove(i);
  }

  public boolean add(ValueType v)
  {
    return this._wrappedArrayList.add(v);
  }

  public void clear()
  {
    this._wrappedArrayList.clear();
  }

  public int size()
  {
    return this._wrappedArrayList.size();
  }

  public boolean isEmpty()
  {
    return this._wrappedArrayList.isEmpty();
  }
}