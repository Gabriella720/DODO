package toolBox.core.utility;

import com.ibm.icu.text.SimpleDateFormat;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Vector;

/**
 * The class pipelines print/println's to several PrintStream. Useful for
 * directing system.out and system.err to external files etc.
 * 
 * @author jiangkai
 * 
 */
public class IORedirect extends PrintStream {
	
	protected static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**The default target output stream*/
	protected PrintStream default_target=null;

	/**The different target output streams*/
	protected Vector<PrintStream> targets=null;
	
	/** */
	protected Vector<Boolean> appendHeader = null;
	
	/** */
	protected String prefix = null;
	
	/**
	 * initializes the object, with a default printstream.
	 */
	public IORedirect(PrintStream s) {
		this(s,"defaultoutput");
	}
	
	public IORedirect(PrintStream s,String p)
	{
		super(s);
		default_target = s;
		prefix = p;
		
		targets = new Vector<PrintStream>();
		appendHeader = new Vector<Boolean>();
		
		clear();
	}
	
	/**
	 * removes all streams and places the default printstream, if any, again in
	 * the list.
	 * 
	 * @see #getDefault()
	 */
	public void clear() {
		
		targets.clear();
		appendHeader.clear();
		
		if (getDefault() != null)
		{
			targets.add(getDefault());
			appendHeader.add(true);
		}
	}

	/**
	 * returns the default printstrean, can be NULL.
	 * 
	 * @return the default printstream
	 * @see #m_Default
	 */
	public PrintStream getDefault() {
		return default_target;
	}

	public void defaultPrint(String x)
	{
		if(default_target!=null)
			default_target.print(x);
	}
	
	public void defaultPrintln(String x)
	{
		if(default_target!=null)
			default_target.println(x);
	}
	
	/**
	 * adds the given PrintStream to the list of streams, with NO timestamp and
	 * NO prefix.
	 * 
	 * @param p
	 *            the printstream to add
	 */
	public void addPrintStream(PrintStream p) {
		addPrintStream(p,false);
	}

	public void addPrintStream(PrintStream p,boolean ah)
	{
		if (!targets.contains(p))
		{
			targets.add(p);
			appendHeader.add(ah);
		}
	}
	
	public void removePrintStream(PrintStream p) {
		if (targets.contains(p))
		{
			int index = targets.indexOf(p);
			targets.remove(index);
			appendHeader.remove(index);
		}
	}

	public boolean containsPrintStream(PrintStream p) {
		return targets.contains(p);
	}

	public int size() {
		return targets.size();
	}

	public void flush() {
		for (PrintStream element : targets)
			element.flush();
	}

	protected void printHeader()
	{
		for(PrintStream element:targets)
			if(appendHeader.get(targets.indexOf(element)))
				element.print(dateformat.format(new Date())+" "+prefix+"\n\t");
	}
	
	@Override
	public void print(int x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(long x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(float x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(double x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(boolean x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(char x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(char[] x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(String x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void print(Object x) {
		printHeader();
		for (PrintStream element : targets)
			element.print(x);
		flush();
	}

	@Override
	public void println(int x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(long x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(float x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(double x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(boolean x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(char x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(char[] x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(String x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	@Override
	public void println(Object x) {
		printHeader();
		for (PrintStream element : targets)
			element.println(x);
		flush();
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at
	 * offset <code>off</code> to this stream. If automatic flushing is enabled
	 * then the <code>flush</code> method will be invoked.
	 * 
	 * <p>
	 * Note that the bytes will be written as given; to write characters that
	 * will be translated according to the platform's default character
	 * encoding, use the <code>print(char)</code> or <code>println(char)</code>
	 * methods.
	 * 
	 * @param buf
	 *            A byte array
	 * @param off
	 *            Offset from which to start taking bytes
	 * @param len
	 *            Number of bytes to write
	 */
	public void write(byte buf[], int off, int len) {
		for (PrintStream element : targets)
			element.write(buf, off, len);
		flush();
	}

	/**
	 * Writes the specified byte to this stream. If the byte is a newline and
	 * automatic flushing is enabled then the <code>flush</code> method will be
	 * invoked.
	 * 
	 * <p>
	 * Note that the byte is written as given; to write a character that will be
	 * translated according to the platform's default character encoding, use
	 * the <code>print(char)</code> or <code>println(char)</code> methods.
	 * 
	 * @param b
	 *            The byte to be written
	 * @see #print(char)
	 * @see #println(char)
	 */
	public void write(int b) {
		for (PrintStream element : targets)
			element.write(b);
		flush();
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		IORedirect stdRedirect = new IORedirect(System.err,"stderr");
//		stdRedirect.addPrintStream(System.err);
		System.setErr(stdRedirect);
		System.err.println("error");
		//stdRedirect.addPrintStream(System.out);
//		System.setOut(stdRedirect);
//		System.out.println("no");
	}
}
