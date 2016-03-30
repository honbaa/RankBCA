package ciir.umass.edu.learning;

public class DenseDataPoint extends DataPoint {

	public DenseDataPoint(String text) {
		super(text);
	}
	
	public DenseDataPoint(DenseDataPoint dp)
	{
		label = dp.label;
		id = dp.id;
		description = dp.description;
		cached = dp.cached;
		setfVals(new float[dp.getfVals().length]);
		System.arraycopy(dp.getfVals(), 0, getfVals(), 0, dp.getfVals().length);
	}
	
	@Override
	public float getFeatureValue(int fid)
	{
		if(fid <= 0 || fid >= getfVals().length)
		{
			System.out.println("Error in DataPoint::getFeatureValue(): requesting unspecified feature, fid=" + fid);
			System.out.println("System will now exit.");
			System.exit(1);
		}
		if(isUnknown(getfVals()[fid]))//value for unspecified feature is 0
			return 0;
		return getfVals()[fid];
	}
	
	@Override
	public void setFeatureValue(int fid, float fval)
	{
		if(fid <= 0 || fid >= getfVals().length)
		{
			System.out.println("Error in DataPoint::setFeatureValue(): feature (id=" + fid + ") not found.");
			System.exit(1);
		}
		getfVals()[fid] = fval;
	}

	@Override
	public void setFeatureVector(float[] dfVals) {
		//fVals = new float[dfVals.length];
		//System.arraycopy(dfVals, 0, fVals, 0, dfVals.length);
		setfVals(dfVals);
	}

	@Override
	public float[] getFeatureVector() {
		return getfVals();
	}
}
