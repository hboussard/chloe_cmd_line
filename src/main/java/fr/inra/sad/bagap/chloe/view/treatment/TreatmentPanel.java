package fr.inra.sad.bagap.chloe.view.treatment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.csvreader.CsvReader;
import com.csvreader.CsvReader.CatastrophicException;
import com.csvreader.CsvReader.FinalizedException;
import fr.inra.sad.bagap.apiland.analysis.matrix.process.metric.MatrixMetricManager;
import fr.inra.sad.bagap.apiland.analysis.matrix.window.shape.WindowShapeType;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Pixel;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.Raster;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.CoordinateManager;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Friction;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.Matrix;
import fr.inra.sad.bagap.apiland.core.space.impl.raster.matrix.MatrixManager;
import fr.inra.sad.bagap.apiland.core.time.Instant;
import fr.inra.sad.bagap.chloe.controller.Controller;
import fr.inra.sad.bagap.chloe.controller.LocalContext;
import fr.inra.sad.bagap.chloe.view.TreatmentTree;
import fr.inra.sad.bagap.chloe.view.wizard.Wizard;
import fr.inra.sad.bagap.chloe.view.wizard.WizardPanel;

public abstract class TreatmentPanel extends WizardPanel {

	private static final long serialVersionUID = 1L; 
	
	// declaration et instanciation des elements statiques
	
	protected static Wizard wizard;
	
	protected static GridBagConstraints c;
	
	protected static Set<Matrix> inputMatrix, /*fM,*/ filterMatrix;
	
	protected static String inputAscii, inputCsv, mapCsv, inputShape, importationFile;
	
	protected static int windowSize, gridSize;
	
	protected static List<Integer> windowSizes, gridSizes;
	
	protected static Map<Integer, Number> values;
	
	protected static Set<Integer> filters, unfilters, vDistances, vFilters;
	
	protected static Set<String> metrics;
	
	protected static Set<String> asciis, shapes;
	
	protected static Friction friction, clusterFriction; 
	
	protected static Matrix frictionMatrix, clusterFrictionMatrix;
	
	protected static int oldNoData;
	
	protected static Set<Pixel> pixels;
	
	protected static Set<String> variables, vmap;
	
	protected static Set<Double> cellsizes;
	
	protected static String attribute;
	
	protected static final int maxQualitativeClassNumber = 200000;
	
	protected static DefaultTableModel tCIM, tCNIM, tNFM, tFM, tDistancesModel, tFiltersModel, tmMatrix/*, tClassificationModel*/;
	
	protected static JLabel title, lAsciiInput, lAsciiFilter, lType, lShape, lSize, lSizeMeters, lGSize, 
	lCsvOutput, lDelta, lDeltaMeters, lMaxRate, lMetrics, lMSMetrics, lOutputFolder, lFilters, lFriction, lNPixel, 
	lConstraint, lValues, lDistances, lValue, lCsvInput, lVariables, lMatrix, lClusters,
	lnodatavalue, lnrows, lxllcorner, lyllcorner, lcellsize, lHeader, lncols, lClassification, lFuzion, lFilterAscii,
	lShapeInput, lAttribute, lCellsize, lEnvelope, lMinX, lMinY, lMaxX, lMaxY, lCorrespondance, lppSelection;

	protected static JButton bAsciiInput, bAsciiFilter, bMatrixAdd, bMatrixRemove, bMatrixUp, bMatrixDown, bViewAsciiInput, bViewAsciiFilter, 
	bCsvOutput, bsAdd, bsRem, bsgAdd, bsgRem, bmAll, bmAdd, bmRem, bmsmAll, bmsmAdd, bmsmRem, bOutputFolder, bFriction, bFrictionCluster, bPixel, bPoint, bRunPixel, 
	bExportPixel, bExportPoint, bCsvInput, bSort, bHeader, bmVAll, bmVAdd, bmVRem, bViewAscii, bFuzion,
	bShapeInput, bsSFAdd, bsSFRem, bCorrespondance, bNewClassification, bRemoveClassification, bCsvMap, bCsvApply, bImportEnvelope, bExportEnvelope;
	
	protected static JTextArea taAsciiInput, taAsciiFilter, taOutputFolder, taCsvOutput, taCsvInput, taPixel, taPoint, taFriction, taFrictionCluster, taShapeInput, taCorrespondance;
	
	protected static JComboBox<String> cbType;  
	
	protected static JComboBox<WindowShapeType> cbShape;
	
	protected static JSpinner spDelta, spMaxRate, spSize, spGSize, spNPixel, spMinDistance, spNoData, spEuclidianDistanceCluster, spFunctionalDistanceCluster,
	spncols, spnrows, spxllcorner, spyllcorner, spcellsize, spnodatavalue, spCellsize, spMinX, spMinY, spMaxX, spMaxY;

	protected static JCheckBox viewAsciiOutput , exportCsv, exportAscii, cbCI, cbCNI, cbMinDistance, cbInterpolate, cbF, cbNF, cbEnvelope;
	
	protected static JTable tSize, tGSize, tLMetrics, tCMetrics, tCI, tCNI, tLMSMetrics, tCMSMetrics, tChanges, tF, tNF, tDistances, tFilters,
	tLVariables, tCVariables, tAttribute, tCellsize, tClassification, tMatrix, tFuzion, tLMap;

	protected static JRadioButton rbPixel, rbPoint, rbGPixel, rbRook, rbQueen, rbEuclidianDistance, rbFunctionalDistance; 
	
	protected static JScrollPane pSize, pGSize, pLMetrics, pCMetrics, pLMSMetrics, pCMSMetrics, pCI, pCNI, pF, pNF, pDistances, pFilters, pClassification,
	pValues, pLVariables, pCVariables, pCellsize, pAttribute, pMatrix, pFuzion, pLMap;

	protected static List<Matrix> inputMatrix2, inputMatrix3;
	
	protected static final Instant t = Instant.get(1, 1, 2000);
	
	static {	
		createComponents();
		actionComponents();	
	}
	//fin de declaration et d'instanciation des elements statiques

	
	public TreatmentPanel(Wizard w){
		wizard = w;
		
		setLayout(new GridBagLayout());
		setBackground(new Color(TreatmentTree.r, TreatmentTree.g, TreatmentTree.b));
	}
	
	protected static Controller getController() {
		return wizard.getController();
	}
	
	protected static void createComponents() {	
		
		inputMatrix = new HashSet<Matrix>();
		inputMatrix2 = new ArrayList<Matrix>();
		inputMatrix3 = new ArrayList<Matrix>();
		pixels = new TreeSet<Pixel>();
		//fM = new HashSet<Matrix>();
		asciis = new HashSet<String>();
		shapes = new HashSet<String>();
		variables = new TreeSet<String>();
		vmap = new TreeSet<String>();
		filterMatrix = new HashSet<Matrix>();
		
		title = new JLabel();
		//title.setForeground(Color.BLUE);
		title.setForeground(Color.GRAY);
		title.setFont(new Font("Verdana", Font.BOLD, 18));
		Font fl = new Font("Verdana", Font.BOLD, 13);
		
		lMatrix = new JLabel("matrix to overlay : ");
		lMatrix.setFont(fl);
		
		lAsciiInput = new JLabel("ascii grid input (file or folder) : ");
		lAsciiInput.setFont(fl);
		
		taAsciiInput = new JTextArea();
		taAsciiInput.setEditable(false);
		
		bAsciiInput = new JButton("Browse");
		
		bViewAsciiInput = new JButton("Visualize");
		bViewAsciiInput.setEnabled(false);
		
		lAsciiFilter = new JLabel("ascii grid filter : ");
		lAsciiFilter.setFont(fl);
		
		taAsciiFilter = new JTextArea();
		taAsciiFilter.setEditable(false);
		
		bAsciiFilter = new JButton("Browse");
		
		bViewAsciiFilter = new JButton("Visualize");
		bViewAsciiFilter.setEnabled(false);
		
		bMatrixAdd = new JButton("Add");
		bMatrixRemove = new JButton("Remove");
		bMatrixRemove.setEnabled(false);
		bMatrixUp = new JButton("UP");
		bMatrixUp.setEnabled(false);
		bMatrixDown = new JButton("DOWN");
		bMatrixDown.setEnabled(false);
		bViewAscii = new JButton("Visualize");
		bViewAscii.setEnabled(false);
		
		lppSelection = new JLabel("pixels/points selection : ");
		lppSelection.setFont(fl);
		
		lCsvInput = new JLabel("csv input (file or folder) : ");
		lCsvInput.setFont(fl);
		
		taCsvInput = new JTextArea();
		
		bCsvInput = new JButton("Browse");
		
		bCsvMap = new JButton("Browse Csv Map");
		
		bCsvApply =new JButton("<- Apply");
		bCsvApply.setEnabled(false);
		
		pLMap = new JScrollPane();
		pLMap.setPreferredSize(new Dimension(400,200));
		pLMap.setMinimumSize(new Dimension(300,100));
		pLMap.setBackground(Color.WHITE);
		Vector<String> columns = new Vector<String>();
		columns.add("map with");
		tLMap = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tLMap.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pLMap.setViewportView(tLMap);
		
		bSort = new JButton("Sort csv file(s)");
		bSort.setEnabled(false);
		
		lVariables = new JLabel("choose variables : ");
		lVariables.setFont(fl);
		
		pLVariables = new JScrollPane();
		pLVariables.setPreferredSize(new Dimension(400,200));
		pLVariables.setMinimumSize(new Dimension(300,100));
		pLVariables.setBackground(Color.WHITE);
		columns = new Vector<String>();
		columns.add("variable");
		tLVariables = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pLVariables.setViewportView(tLVariables);
		
		bmVAll = new JButton(">> add all");
		bmVAll.setEnabled(false);
		
		bmVAdd = new JButton(">> add selected");
		bmVAdd.setEnabled(false);
		
		bmVRem = new JButton("remove selected <<");
		bmVRem.setEnabled(false);
		
		pCVariables = new JScrollPane();
		pCVariables.setPreferredSize(new Dimension(400,200));
		pCVariables.setMinimumSize(new Dimension(300,100));
		pCVariables.setBackground(Color.WHITE);
		tCVariables = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pCVariables.setViewportView(tCVariables);
		
		lHeader = new JLabel("ascii grid header : ");
		lHeader.setFont(fl);
		
		lncols = new JLabel("columns count (ncols) : ");
		lncols.setFont(fl);
		
		spncols = new JSpinner(new SpinnerNumberModel(100, 1, 100000, 1));
		spncols.setEnabled(false);
		
		lnrows = new JLabel("rows count (nrows) : ");
		lnrows.setFont(fl);
		
		spnrows = new JSpinner(new SpinnerNumberModel(100, 1, 100000, 1));
		spnrows.setEnabled(false);
		
		lxllcorner = new JLabel("X bottom left corner coordinate (xllcorner) : ");
		lxllcorner.setFont(fl);
		
		spxllcorner = new JSpinner(new SpinnerNumberModel(0.0, -999999999, 999999999, 1));
		spxllcorner.setEnabled(false);
		
		lyllcorner = new JLabel("Y bottom left corner coordinate (yllcorner) : ");
		lyllcorner.setFont(fl);
		
		spyllcorner = new JSpinner(new SpinnerNumberModel(0.0, -999999999, 999999999, 1));
		spyllcorner.setEnabled(false);
		
		lcellsize = new JLabel("cell size (cellsize) : ");
		lcellsize.setFont(fl);
		
		spcellsize = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
		spcellsize.setEnabled(false);
		
		lnodatavalue = new JLabel("value if no-data (NODATA_value) : ");
		lnodatavalue.setFont(fl);
		
		spnodatavalue = new JSpinner(new SpinnerNumberModel(-1, -999999999, 999999999, 1));
		spnodatavalue.setEnabled(false);
		
		bHeader = new JButton("Import header from ascii file");
		bHeader.setEnabled(false);
		
		lValues = new JLabel("values to search and replace : ");
		lValues.setFont(fl);
		
		lDistances = new JLabel("distance from value(s) : ");
		lDistances.setFont(fl);
		
		lFilterAscii = new JLabel("filter value(s) : ");
		lFilterAscii.setFont(fl);
		
		lClusters = new JLabel("clusters from value(s) : ");
		lClusters.setFont(fl);
		
		rbRook = new JRadioButton("rook neighbourhood");
		rbRook.setFont(fl);
		rbRook.setEnabled(false);
		
		rbQueen = new JRadioButton("queen neighbourhood");
		rbQueen.setFont(fl);
		rbQueen.setEnabled(false);
		
		rbEuclidianDistance = new JRadioButton("euclidian distance (in meters): ");
		rbEuclidianDistance.setFont(fl);
		rbEuclidianDistance.setEnabled(false);
		
		spEuclidianDistanceCluster = new JSpinner(new SpinnerNumberModel(Raster.getCellSize(), Raster.getCellSize(), 999999999, 1));
		spEuclidianDistanceCluster.setEnabled(false);
		
		rbFunctionalDistance = new JRadioButton("functional distance (in meters): ");
		rbFunctionalDistance.setFont(fl);
		rbFunctionalDistance.setEnabled(false);
		
		spFunctionalDistanceCluster = new JSpinner(new SpinnerNumberModel(Raster.getCellSize(), Raster.getCellSize(), 999999999, 1));
		spFunctionalDistanceCluster.setEnabled(false);
		
		ButtonGroup bgCluster = new ButtonGroup();
		bgCluster.add(rbRook);
		bgCluster.add(rbQueen);
		bgCluster.add(rbEuclidianDistance);
		bgCluster.add(rbFunctionalDistance);
		
		pValues = new JScrollPane();
		pValues.setPreferredSize(new Dimension(400,200));
		pValues.setMinimumSize(new Dimension(300,100));
		pValues.setBackground(Color.WHITE);
		pValues.setEnabled(false);
		
		lValue  = new JLabel("nodata value : ");
		lValue.setFont(fl);
		
		spNoData = new JSpinner(new SpinnerNumberModel(Raster.getNoDataValue(), -99999, 100000, 1));
		spNoData.setEnabled(false);
		
		lShape = new JLabel("window shape : ");
		lShape.setFont(fl);
		
		cbShape = new JComboBox<WindowShapeType>(WindowShapeType.values());
		cbShape.setSelectedIndex(0);
		cbShape.setEnabled(false);
		
		lFriction = new JLabel("friction file :");
		lFriction.setFont(fl);
		
		taFriction = new JTextArea();
		taFriction.setEnabled(false);
		
		taFrictionCluster = new JTextArea();
		taFrictionCluster.setEnabled(false);
		
		bFriction = new JButton("Browse");
		bFriction.setEnabled(false);
		
		bFrictionCluster = new JButton("Browse");
		bFrictionCluster.setEnabled(false);
		
		lType = new JLabel("type of metrics : ");
		lType.setFont(fl);
		
		cbType = new JComboBox<String>(new String[]{"values metrics", "couples metrics", "patches metrics", "connectivity metrics", "diversity metrics", "landscape grain", "quantitative metrics"});
		cbType.setSelectedIndex(0);
		cbType.setEnabled(false);
		
		lMaxRate = new JLabel("maximum rate of missing values : ");
		lMaxRate.setFont(fl);
		
		spMaxRate = new JSpinner(new SpinnerNumberModel(100, 0, 100, 1));
		spMaxRate.setEnabled(false);
		
		lSize = new JLabel("window sizes (pixels) : ");
		lSize.setFont(fl);
		
		spSize = new JSpinner(new SpinnerNumberModel(3, 3, 999999999, 2));
		spSize.setEnabled(false);
		
		lSizeMeters = new JLabel("");
		lSizeMeters.setFont(fl);
		
		lGSize = new JLabel("grid sizes (pixels) : ");
		lGSize.setFont(fl);
		
		spGSize = new JSpinner(new SpinnerNumberModel(2, 2, Integer.MAX_VALUE, 1));
		spGSize.setEnabled(false);
		
		bsAdd = new JButton("Add");
		bsAdd.setEnabled(false);
		
		bsgAdd = new JButton("Add");
		bsgAdd.setEnabled(false);
		
		pSize = new JScrollPane();
		pSize.setPreferredSize(new Dimension(200,100));
		pSize.setMinimumSize(new Dimension(200,100));
		pSize.setBackground(Color.WHITE);
		columns.clear();
		columns.add("size");
		tSize = new JTable(new Vector<Vector<String>>(), columns);
		tSize.setEnabled(false);
		pSize.setViewportView(tSize);
		
		pGSize = new JScrollPane();
		pGSize.setPreferredSize(new Dimension(200,100));
		pGSize.setMinimumSize(new Dimension(200,100));
		pGSize.setBackground(Color.WHITE);
		columns.clear();
		columns.add("size");
		tGSize = new JTable(new Vector<Vector<String>>(), columns);
		tGSize.setEnabled(false);
		pGSize.setViewportView(tGSize);
		
		bsRem = new JButton("Remove");
		bsRem.setEnabled(false);
		
		bsgRem = new JButton("Remove");
		bsgRem.setEnabled(false);
		
		lDelta = new JLabel("delta of displacement (pixels) : ");
		lDelta.setFont(fl);
		
		spDelta = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
		spDelta.setEnabled(false);
		
		lDeltaMeters = new JLabel("");
		lDeltaMeters.setFont(fl);
		
		cbInterpolate = new JCheckBox("interpolate values");
		cbInterpolate.setFont(fl);
		cbInterpolate.setEnabled(false);
		
		rbPixel = new JRadioButton("             pixel(s) file : ");
		rbPixel.setFont(fl);
		rbPixel.setEnabled(false);
		
		taPixel = new JTextArea();
		taPixel.setEnabled(false);
		
		bPixel = new JButton("Browse");
		bPixel.setEnabled(false);
		
		rbPoint = new JRadioButton("            point(s) file : ");
		rbPoint.setFont(fl);
		rbPoint.setEnabled(false);
		
		taPoint = new JTextArea();
		taPoint.setEnabled(false);
		
		bPoint = new JButton("Browse");
		bPoint.setEnabled(false);
		
		rbGPixel = new JRadioButton("generated pixel(s) : ");
		rbGPixel.setFont(fl);
		rbGPixel.setEnabled(false);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbPixel);
		bg.add(rbPoint);
		bg.add(rbGPixel);
		
		lNPixel = new JLabel("number of pixel(s) to generate :");
		lNPixel.setFont(fl);
		
		spNPixel = new JSpinner(new SpinnerNumberModel(100, 1, Integer.MAX_VALUE, 1));
		spNPixel.setEnabled(false);
		
		lConstraint = new JLabel("constraints : ");
		lConstraint.setFont(fl);
		
		cbMinDistance = new JCheckBox("minimum distance (pixels) : ");
		cbMinDistance.setFont(fl);
		cbMinDistance.setEnabled(false);
		
		spMinDistance = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
		spMinDistance.setEnabled(false);
		
		lFilters = new JLabel("filters (to indicate if specific) : ");
		lFilters.setFont(fl);
		
		cbF = new JCheckBox("analyze only : ");
		cbF.setFont(fl);
		cbF.setEnabled(false);
		
		pF = new JScrollPane();
		pF.setPreferredSize(new Dimension(100,100));
		pF.setMinimumSize(new Dimension(100,100));
		pF.setBackground(Color.WHITE);
		
		columns.clear();
		columns.add("values");
		tFM = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tF = new JTable(tFM);
		tF.setEnabled(false);
		pF.setViewportView(tF);
		
		cbNF = new JCheckBox("do not analyze : ");
		cbNF.setFont(fl);
		cbNF.setEnabled(false);
		
		pNF = new JScrollPane();
		pNF.setPreferredSize(new Dimension(100,100));
		pNF.setMinimumSize(new Dimension(100,100));
		pNF.setBackground(Color.WHITE);
		
		tNFM = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tNF = new JTable(tNFM);
		tNF.setEnabled(false);
		pNF.setViewportView(tNF);
		
		pDistances = new JScrollPane();
		pDistances.setPreferredSize(new Dimension(100,150));
		pDistances.setMinimumSize(new Dimension(100,150));
		pDistances.setBackground(Color.WHITE);
		
		tDistancesModel = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tDistances = new JTable(tDistancesModel);
		tDistances.setEnabled(false);
		pDistances.setViewportView(tDistances);
	    
		pFilters = new JScrollPane();
		pFilters.setPreferredSize(new Dimension(100,150));
		pFilters.setMinimumSize(new Dimension(100,150));
		pFilters.setBackground(Color.WHITE);
		
		tFiltersModel = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tFilters = new JTable(tFiltersModel);
		tFilters.setEnabled(false);
		pFilters.setViewportView(tFilters);
		
		columns.clear();
		columns.add("matrix");
		pMatrix = new JScrollPane();
		pMatrix.setPreferredSize(new Dimension(600,300));
		pMatrix.setMinimumSize(new Dimension(600,300));
		pMatrix.setBackground(Color.WHITE);
		
		tmMatrix = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tMatrix = new JTable(tmMatrix);
		tMatrix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tMatrix.setEnabled(false);
		pMatrix.setViewportView(tMatrix);
		
		lClassification = new JLabel("new classification : ");
		lClassification.setFont(fl);
		
		pClassification = new JScrollPane();
		pClassification.setPreferredSize(new Dimension(350,150));
		pClassification.setMinimumSize(new Dimension(350,150));
		pClassification.setBackground(Color.WHITE);
		
		Vector<String> columns2 = new Vector<String>();
		columns2.add("domain");
		columns2.add("class");
		tClassification = new JTable(null, columns2);
		tClassification.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		tClassification.setEnabled(false);
		tClassification.setFont(new Font("TimesRoman", Font.BOLD, 16));
		pClassification.setViewportView(tClassification);
		
		bNewClassification = new JButton("new class");
		bNewClassification.setEnabled(false);
		
		bRemoveClassification = new JButton("remove last");
		bRemoveClassification.setEnabled(false);
		
		lFuzion = new JLabel("matrix to combine : ");
		lFuzion.setFont(fl);
		
		bFuzion = new JButton("Add matrix");
		
		pFuzion = new JScrollPane();
		pFuzion.setPreferredSize(new Dimension(200,150));
		pFuzion.setMinimumSize(new Dimension(200,150));
		pFuzion.setBackground(Color.WHITE);
		
		Vector<String> columns3 = new Vector<String>();
		columns3.add("matrix");
		columns3.add("factor");
		
		DefaultTableModel tFuzionM = new DefaultTableModel(new Vector<Vector<String>>(), columns3){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				switch (column) {
		         case 0:
		        	 return false;
		         case 1:
		             return true;
		         default:
		             return false;
		      }
			}
		};
		
		tFuzion = new JTable(tFuzionM);
		tFuzion.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		tFuzion.setEnabled(false);
		pFuzion.setViewportView(tFuzion);
		
		cbCI = new JCheckBox("analyze only : ");
		cbCI.setFont(fl);
		cbCI.setEnabled(false);
		
		pCI = new JScrollPane();
		pCI.setPreferredSize(new Dimension(100,100));
		pCI.setMinimumSize(new Dimension(100,100));
		pCI.setBackground(Color.WHITE);
		
		columns.clear();
		columns.add("values");
		tCIM = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tCI = new JTable(tCIM);
		tCI.setEnabled(false);
		pCI.setViewportView(tCI);
		
		cbCNI = new JCheckBox("do not analyze : ");
		cbCNI.setFont(fl);
		cbCNI.setEnabled(false);
		
		pCNI = new JScrollPane();
		pCNI.setPreferredSize(new Dimension(100,100));
		pCNI.setMinimumSize(new Dimension(100,100));
		pCNI.setBackground(Color.WHITE);
		
		tCNIM = new DefaultTableModel(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tCNI = new JTable(tCNIM);
		tCNI.setEnabled(false);
		pCNI.setViewportView(tCNI);
		
		bRunPixel = new JButton("Generate");
		bRunPixel.setEnabled(false);
		
		bExportPixel = new JButton("Export pixels");
		bExportPixel.setEnabled(false);
		
		bExportPoint = new JButton("Export points");
		bExportPoint.setEnabled(false);
		
		lMetrics = new JLabel("metrics : ");
		lMetrics.setFont(fl);
		
		pLMetrics = new JScrollPane();
		pLMetrics.setPreferredSize(new Dimension(300,200));
		pLMetrics.setMinimumSize(new Dimension(300,100));
		pLMetrics.setBackground(Color.WHITE);
		columns.clear();
		columns.add("metric");
		tLMetrics = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pLMetrics.setViewportView(tLMetrics);
		
		bmAll = new JButton(">> add all");
		bmAll.setEnabled(false);
		
		bmAdd = new JButton(">> add selected");
		bmAdd.setEnabled(false);
		
		bmRem = new JButton("remove selected <<");
		bmRem.setEnabled(false);
		
		pCMetrics = new JScrollPane();
		pCMetrics.setPreferredSize(new Dimension(300,200));
		pCMetrics.setMinimumSize(new Dimension(300,100));
		pCMetrics.setBackground(Color.WHITE);
		tCMetrics = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pCMetrics.setViewportView(tCMetrics);
		
		lMSMetrics = new JLabel("multi-scales metrics : ");
		lMSMetrics.setFont(fl);
		
		pLMSMetrics = new JScrollPane();
		pLMSMetrics.setPreferredSize(new Dimension(300,100));
		pLMSMetrics.setMinimumSize(new Dimension(300,100));
		pLMSMetrics.setBackground(Color.WHITE);
		tLMSMetrics = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pLMSMetrics.setViewportView(tLMSMetrics);
		
		bmsmAll = new JButton(">> add all");
		bmsmAll.setEnabled(false);
		
		bmsmAdd = new JButton(">> add selected");
		bmsmAdd.setEnabled(false);
		
		bmsmRem = new JButton("remove selected <<");
		bmsmRem.setEnabled(false);
		
		pCMSMetrics = new JScrollPane();
		pCMSMetrics.setPreferredSize(new Dimension(300,100));
		pCMSMetrics.setMinimumSize(new Dimension(300,100));
		pCMSMetrics.setBackground(Color.WHITE);
		tCMSMetrics = new JTable(new Vector<Vector<String>>(), columns){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pCMSMetrics.setViewportView(tCMSMetrics);
		
		lOutputFolder = new JLabel("output folder : ");
		lOutputFolder.setFont(fl);
		
		taOutputFolder = new JTextArea();
		taOutputFolder.setEnabled(false);
		
		bOutputFolder = new JButton("Browse");
		bOutputFolder.setEnabled(false);
		
		lCsvOutput = new JLabel("csv output file : ");
		lCsvOutput.setFont(fl);
		
		taCsvOutput = new JTextArea();
		taCsvOutput.setEnabled(false);
		
		bCsvOutput = new JButton("Browse");
		bCsvOutput.setEnabled(false);
		
		viewAsciiOutput = new JCheckBox("visualize ascii grid output(s)");
		viewAsciiOutput.setFont(fl);
		viewAsciiOutput.setSelected(true);
		viewAsciiOutput.setEnabled(false);
		
		exportCsv = new JCheckBox("export csv file(s)");
		exportCsv.setFont(fl);
		exportCsv.setSelected(true);
		exportCsv.setEnabled(false);
		
		exportAscii = new JCheckBox("export ascii grid output(s)");
		exportAscii.setFont(fl);
		exportAscii.setSelected(true);
		exportAscii.setEnabled(false);
		
		lShapeInput = new JLabel("input shapefile (file or folder) : ");
		lShapeInput.setFont(fl);
		
		taShapeInput = new JTextArea();
		
		bShapeInput = new JButton("Browse");
		
		lAttribute = new JLabel("attribute to export : ");
		lAttribute.setFont(fl);
		
		pAttribute = new JScrollPane();
		pAttribute.setPreferredSize(new Dimension(100,100));
		pAttribute.setMinimumSize(new Dimension(100,100));
		pAttribute.setBackground(Color.WHITE);
		
		columns2.clear();
		columns2.add("attribute");
		columns2.add("type");
		tAttribute = new JTable(new Vector<Vector<String>>(), columns2){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tAttribute.setEnabled(false);
		tAttribute.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pAttribute.setViewportView(tAttribute);
		
		lCorrespondance = new JLabel("add a lookup table :");
		lCorrespondance.setFont(fl);
		
		taCorrespondance = new JTextArea();
		taCorrespondance.setEnabled(false);
		
		bCorrespondance = new JButton("Browse");
		bCorrespondance.setEnabled(false);
		
		lCellsize = new JLabel("cellsize : ");
		lCellsize.setFont(fl);
		
		spCellsize = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 99999999.0, 1));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spCellsize, "0.0");
	    spCellsize.setEditor(editor);
		spCellsize.setEnabled(false);
		
		bsSFAdd = new JButton("Add");
		bsSFAdd.setEnabled(false);
		
		pCellsize = new JScrollPane();
		pCellsize.setPreferredSize(new Dimension(200,100));
		pCellsize.setMinimumSize(new Dimension(200,100));
		pCellsize.setBackground(Color.WHITE);
		columns.clear();
		columns.add("cellsize");
		tCellsize = new JTable(new Vector<Vector<String>>(), columns);
		tCellsize.setEnabled(false);
		pCellsize.setViewportView(tCellsize);
		
		bsSFRem = new JButton("Remove");
		bsSFRem.setEnabled(false);
		
		lEnvelope = new JLabel("specific envelope : ");
		lEnvelope.setFont(fl);
	
		cbEnvelope = new JCheckBox("");
		cbEnvelope.setFont(fl);
		cbEnvelope.setEnabled(false);
		
		lMinX = new JLabel("min X : ");
		lMinX.setFont(fl);
		
		spMinX = new JSpinner(new SpinnerNumberModel(0.0, -999999999.0, 999999999.0, 10));
	    JSpinner.NumberEditor editorminx = new JSpinner.NumberEditor(spMinX, "0.00000");
	    spMinX.setEditor(editorminx);
	    spMinX.setEnabled(false);
	    
	    lMinY = new JLabel("min Y : ");
		lMinY.setFont(fl);
	    
	    spMinY = new JSpinner(new SpinnerNumberModel(0.0, -999999999.0, 999999999.0, 10));
	    JSpinner.NumberEditor editorminy = new JSpinner.NumberEditor(spMinY, "0.00000");
	    spMinY.setEditor(editorminy);
	    spMinY.setEnabled(false);
	    
	    lMaxX = new JLabel("max X : ");
		lMaxX.setFont(fl);
	    
	    spMaxX = new JSpinner(new SpinnerNumberModel(0.0, -999999999.0, 999999999.0, 10));
	    JSpinner.NumberEditor editormaxx = new JSpinner.NumberEditor(spMaxX, "0.00000");
	    spMaxX.setEditor(editormaxx);
	    spMaxX.setEnabled(false);
	    
	    lMaxY = new JLabel("max Y : ");
		lMaxY.setFont(fl);
		
	    spMaxY = new JSpinner(new SpinnerNumberModel(0.0, -999999999.0, 999999999.0, 10));
	    JSpinner.NumberEditor editormaxy = new JSpinner.NumberEditor(spMaxY, "0.00000");
	    spMaxY.setEditor(editormaxy);
	    spMaxY.setEnabled(false);
	    
	    bImportEnvelope = new JButton("Import envelope");
	    bImportEnvelope.setEnabled(false);
	    
	    bExportEnvelope = new JButton("Export envelope");
	    bExportEnvelope.setEnabled(false);
	}
	
	protected static void actionComponents() {
		
		bAsciiInput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					inputAscii = fc.getSelectedFile().toString();
					inputMatrix.clear();
					asciis.clear();
					
					getController().importAsciiGrid((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), inputMatrix, inputAscii);
				}
			}
		});
		
		bAsciiFilter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					inputAscii = fc.getSelectedFile().toString();
					filterMatrix.clear();
					
					getController().importAsciiGridFilter((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), filterMatrix, inputAscii);
				}
			}
		});
		
		bMatrixAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					String inputAscii2 = fc.getSelectedFile().toString();
					
					getController().importAsciiGrid2((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), inputMatrix2, inputAscii2);
				}
			}
		});
		
		bFuzion.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					String inputAscii3 = fc.getSelectedFile().toString();
					
					getController().importAsciiGrid3((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), inputMatrix3, inputAscii3);
				}
			}
		});
		
		bViewAsciiInput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File(taAsciiInput.getText());
				if(file.isDirectory()){
					for(File f : file.listFiles()){
						if(f.isFile() && f.getName().endsWith(".asc")){
							MatrixManager.visualize(f.getAbsolutePath());
						}
					}
				}else{
					MatrixManager.visualize(taAsciiInput.getText());
				}
			}
		});
		
		bViewAsciiFilter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File(taAsciiFilter.getText());
				if(file.isDirectory()){
					for(File f : file.listFiles()){
						if(f.isFile() && f.getName().endsWith(".asc")){
							MatrixManager.visualize(f.getAbsolutePath());
						}
					}
				}else{
					MatrixManager.visualize(taAsciiFilter.getText());
				}
			}
		});
		
		cbType.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					displayMetrics(e.getItem().toString());
				}
			}
		});
		
		cbShape.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					if(((WindowShapeType) e.getItem()).equals(WindowShapeType.FUNCTIONAL)){
						taFriction.setEnabled(true);
						bFriction.setEnabled(true);
						setEnabledDMax(true);
					}else{
						taFriction.setEnabled(false);
						bFriction.setEnabled(false);
						setEnabledDMax(false);
					}
				}
			}
		});
		
		spSize.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				displayWindowSizeInMeters((Integer) spSize.getValue() * inputMatrix.iterator().next().cellsize());
				if(((WindowShapeType) cbShape.getSelectedItem()).equals(WindowShapeType.FUNCTIONAL)){
					setEnabledDMax(true);
				}
			}
		});
		
		bFriction.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					String file = fc.getSelectedFile().toString();
					taFriction.setText(file);
					
					//fM.clear();
					friction = null;
					frictionMatrix = null;
					
					if(file.endsWith(".asc")){
						getController().importAsciiGridFriction((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), frictionMatrix, fc.getSelectedFile().toString());
					}
				}
			}
		});
		
		bFrictionCluster.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					String file = fc.getSelectedFile().toString();
					taFrictionCluster.setText(file);
					
					//fM.clear();
					clusterFriction = null;
					clusterFrictionMatrix = null;
					
					if(file.endsWith(".asc")){
						getController().importAsciiGridFriction((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), clusterFrictionMatrix, fc.getSelectedFile().toString());
					}
				}
			}
		});
		
		bsRem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int i=0;
				for(int r : tSize.getSelectedRows()){
					r = r - (i++);
					((DefaultTableModel) tSize.getModel()).removeRow(r);
				}
			}
		});
		
		bsgAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int s = (Integer) spGSize.getValue();
				spGSize.setValue(s+1);
				for(int i=0; i<tGSize.getRowCount(); i++){
					if(s == (Integer) tGSize.getValueAt(i, 0)){
						return;
					}
				}
				Vector<Integer> v = new Vector<Integer>();
				v.add(s);
				((DefaultTableModel) tGSize.getModel()).addRow(v);
			}
		});
		
		bsgRem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int i=0;
				for(int r : tGSize.getSelectedRows()){
					r = r - (i++);
					((DefaultTableModel) tGSize.getModel()).removeRow(r);
				}
			}
		});
		
		spDelta.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				displayDeltaInMeters();
				if(((Integer)spDelta.getValue()) > 1){
					cbInterpolate.setEnabled(true);
				}else{
					cbInterpolate.setEnabled(false);
				}
			}
		});
		
		rbPixel.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(rbPixel.isSelected()){
					taPixel.setEnabled(true);
					bPixel.setEnabled(true);
				}else{
					taPixel.setEnabled(false);
					bPixel.setEnabled(false);
				}
			}
		});
		
		bPixel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					taPixel.setText(fc.getSelectedFile().toString());
					
					pixels = CoordinateManager.initWithPixels(fc.getSelectedFile().toString());
					
					taPoint.setEnabled(false);
					bPixel.setEnabled(false);
					bPoint.setEnabled(false);
					spNPixel.setEnabled(false);
					cbMinDistance.setEnabled(false);
					spMinDistance.setEnabled(false);
					cbCI.setEnabled(false);
					pCI.setEnabled(false);
					cbCNI.setEnabled(false);
					pCNI.setEnabled(false);
					bRunPixel.setEnabled(false);
					
					bExportPixel.setEnabled(true);
					bExportPoint.setEnabled(true);
				}
			}
		});
		
		rbPoint.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(rbPoint.isSelected()){
					taPoint.setEnabled(true);
					bPoint.setEnabled(true);
				}else{
					taPoint.setEnabled(false);
					bPoint.setEnabled(false);
				}
			}
		});
		
		bPoint.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					taPoint.setText(fc.getSelectedFile().toString());
					
					pixels = CoordinateManager.initWithPoints(inputMatrix.iterator().next(), fc.getSelectedFile().toString());
					
					taPixel.setEnabled(false);
					bPixel.setEnabled(false);
					bPoint.setEnabled(false);
					spNPixel.setEnabled(false);
					cbMinDistance.setEnabled(false);
					spMinDistance.setEnabled(false);
					cbCI.setEnabled(false);
					pCI.setEnabled(false);
					cbCNI.setEnabled(false);
					pCNI.setEnabled(false);
					bRunPixel.setEnabled(false);
					
					bExportPixel.setEnabled(true);
					bExportPoint.setEnabled(true);
				}
			}
		});
		
		rbGPixel.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(rbGPixel.isSelected()){
					spNPixel.setEnabled(true);
					cbMinDistance.setEnabled(true);
					cbCI.setEnabled(true);
					cbCNI.setEnabled(true);
					bRunPixel.setEnabled(true);
				}else{
					spNPixel.setEnabled(false);
					cbMinDistance.setEnabled(false);
					spMinDistance.setEnabled(false);
					cbCI.setEnabled(false);
					tCI.setEnabled(false);
					cbCNI.setEnabled(false);
					tCNI.setEnabled(false);
					bRunPixel.setEnabled(false);
					bRunPixel.setEnabled(false);
				}
			}
		});
		
		cbMinDistance.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(cbMinDistance.isSelected()){
					spMinDistance.setEnabled(true);
				}else{
					spMinDistance.setEnabled(false);
				}
			}
		});
		
		cbF.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(cbF.isSelected()){
					tF.setEnabled(true);
				}else{
					tF.setEnabled(false);
				}
			}
		});
		
		cbNF.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(cbNF.isSelected()){
					tNF.setEnabled(true);
				}else{
					tNF.setEnabled(false);
				}
			}
		});
		
		cbCI.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(cbCI.isSelected()){
					tCI.setEnabled(true);
				}else{
					tCI.setEnabled(false);
				}
			}
		});
		
		cbCNI.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(cbCNI.isSelected()){
					tCNI.setEnabled(true);
				}else{
					tCNI.setEnabled(false);
				}
			}
		});
		
		bRunPixel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int distance = -1;
				if(cbMinDistance.isSelected()){
					distance = 0;
					if(cbMinDistance.isSelected()){
						distance = (Integer) spMinDistance.getValue();
					}
				}
				
				Set<Integer> with = null;
				if(cbCI.isSelected()){
					with = new HashSet<Integer>();
					for(int r : tCI.getSelectedRows()){
						with.add((Integer) tCI.getModel().getValueAt(r, 0));
					}
				}
				
				Set<Integer> without = null;
				if(cbCNI.isSelected()){
					without = new HashSet<Integer>();
					for(int r : tCNI.getSelectedRows()){
						without.add((Integer) tCNI.getModel().getValueAt(r, 0));
					}
				}
				
				try{
					getController().generatePixels(pixels, inputMatrix.iterator().next(), (Integer) spNPixel.getValue(), distance, with, without);
				}catch(IllegalArgumentException ex){
					ex.printStackTrace();
				}
			}
		});
		
		bExportPixel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showSaveDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					String file = fc.getSelectedFile().toString();
					if(!file.endsWith(".txt") && !file.endsWith(".csv")){
						file += ".csv";
					}
					
					CoordinateManager.savePixels(pixels, inputMatrix.iterator().next(), file);
				}
			}
		});
		
		bExportPoint.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showSaveDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					String file = fc.getSelectedFile().toString();
					if(!file.endsWith(".txt") && !file.endsWith(".csv")){
						file += ".csv";
					}
					
					CoordinateManager.savePoints(pixels, inputMatrix.iterator().next(), file);
				}
			}
		});
		
		tLMetrics.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					Vector<String> v;
					Set<String> m = new TreeSet<String>();
					int i=0;
					for(int r : tLMetrics.getSelectedRows()){
						r = r - (i++);
						v = new Vector<String>();
						v.add((String) tLMetrics.getModel().getValueAt(r, 0));
						m.add((String) tLMetrics.getModel().getValueAt(r, 0));
						((DefaultTableModel) tCMetrics.getModel()).addRow(v);
						((DefaultTableModel) tLMetrics.getModel()).removeRow(r);
					}
				}
			}
		});
		
		tCMetrics.getModel().addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType() == TableModelEvent.INSERT){
					Set<String> m = new TreeSet<String>();
					for(int r=e.getFirstRow(); r<e.getLastRow()+1; r++){
						m.add((String) tCMetrics.getModel().getValueAt(r, 0));
					}
					displayMultiScalesMetrics(m);
				}
			}
		});
		  
		tMatrix.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					bMatrixRemove.setEnabled(false);
					bMatrixUp.setEnabled(false);
					bMatrixDown.setEnabled(false);
					bViewAscii.setEnabled(false);
		        }
		        else{
		            bMatrixRemove.setEnabled(true);
					bMatrixUp.setEnabled(true);
					bMatrixDown.setEnabled(true);
					bViewAscii.setEnabled(true);
		        }
			}
		});
		
		bViewAscii.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tMatrix.getSelectedRows()[0];
				String ascii = (String) tMatrix.getModel().getValueAt(row, 0);
				MatrixManager.visualize(ascii);
			}
		});
		
		bMatrixRemove.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String ascii = (String) tMatrix.getModel().getValueAt(tMatrix.getSelectedRows()[0], 0);
				((DefaultTableModel) tMatrix.getModel()).removeRow(tMatrix.getSelectedRows()[0]);
				Iterator<Matrix> ite = inputMatrix2.iterator();
				while(ite.hasNext()){
					Matrix m = ite.next();
					if(m.getFile().equalsIgnoreCase(ascii)){
						ite.remove();
						break;
					}
				}
			}
		});
		
		bMatrixUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tMatrix.getSelectedRows()[0];
				if(row > 0){
					String ascii = (String) tMatrix.getModel().getValueAt(row, 0);
					((DefaultTableModel) tMatrix.getModel()).removeRow(row);
					Vector<String> v = new Vector<String>();
					v.add(ascii);
					((DefaultTableModel) tMatrix.getModel()).insertRow(row - 1, v);
					tMatrix.getSelectionModel().setSelectionInterval(row - 1, row - 1);
					
					Matrix m = inputMatrix2.get(row - 1);
					inputMatrix2.set(row-1, inputMatrix2.get(row));
					inputMatrix2.set(row, m);
				}
			}
		});
		
		bMatrixDown.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tMatrix.getSelectedRows()[0];
				if(row < ((DefaultTableModel) tMatrix.getModel()).getRowCount() - 1){
					String ascii = (String) tMatrix.getModel().getValueAt(row, 0);
					((DefaultTableModel) tMatrix.getModel()).removeRow(row);
					Vector<String> v = new Vector<String>();
					v.add(ascii);
					((DefaultTableModel) tMatrix.getModel()).insertRow(row + 1, v);
					tMatrix.getSelectionModel().setSelectionInterval(row + 1, row + 1);
					
					Matrix m = inputMatrix2.get(row);
					inputMatrix2.set(row, inputMatrix2.get(row+1));
					inputMatrix2.set(row+1, m);
				}
			}
		});
		
		bmAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				while(tLMetrics.getRowCount() > 0){
					v = new Vector<String>();
					v.add((String) tLMetrics.getModel().getValueAt(0, 0));
					((DefaultTableModel) tCMetrics.getModel()).addRow(v);
					((DefaultTableModel) tLMetrics.getModel()).removeRow(0);
				}
			}
		});
		
		bmAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				int i=0;
				for(int r : tLMetrics.getSelectedRows()){
					r = r - (i++);
					v = new Vector<String>();
					v.add((String) tLMetrics.getModel().getValueAt(r, 0));
					((DefaultTableModel) tCMetrics.getModel()).addRow(v);
					((DefaultTableModel) tLMetrics.getModel()).removeRow(r);
				}
			}
		});
		
		bmRem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				Set<String> m = new TreeSet<String>();
				int i=0;
				for(int r : tCMetrics.getSelectedRows()){
					r = r - (i++);
					v = new Vector<String>();
					v.add((String) tCMetrics.getModel().getValueAt(r, 0));
					m.add((String) tCMetrics.getModel().getValueAt(r, 0));
					((DefaultTableModel) tLMetrics.getModel()).addRow(v);
					((DefaultTableModel) tCMetrics.getModel()).removeRow(r);
				}
				
				removeMultiScalesMetrics(m);
			}
		});
		
		bmsmAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				while(tLMSMetrics.getRowCount() > 0){
					v = new Vector<String>();
					v.add((String) tLMSMetrics.getModel().getValueAt(0, 0));
					((DefaultTableModel) tCMSMetrics.getModel()).addRow(v);
					((DefaultTableModel) tLMSMetrics.getModel()).removeRow(0);
				}
			}
		});
		
		bmsmAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				int i=0;
				for(int r : tLMSMetrics.getSelectedRows()){
					r = r - (i++);
					v = new Vector<String>();
					v.add((String) tLMSMetrics.getModel().getValueAt(r, 0));
					((DefaultTableModel) tCMSMetrics.getModel()).addRow(v);
					((DefaultTableModel) tLMSMetrics.getModel()).removeRow(r);
				}
			}
		});
		
		bmsmRem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				int i=0;
				for(int r : tCMSMetrics.getSelectedRows()){
					r = r - (i++);
					v = new Vector<String>();
					v.add((String) tCMSMetrics.getModel().getValueAt(r, 0));
					((DefaultTableModel) tLMSMetrics.getModel()).addRow(v);
					((DefaultTableModel) tCMSMetrics.getModel()).removeRow(r);
				}
			}
		});
		
		tLMSMetrics.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					Vector<String> v;
					int i=0;
					for(int r : tLMSMetrics.getSelectedRows()){
						r = r - (i++);
						v = new Vector<String>();
						v.add((String) tLMSMetrics.getModel().getValueAt(r, 0));
						((DefaultTableModel) tCMSMetrics.getModel()).addRow(v);
						((DefaultTableModel) tLMSMetrics.getModel()).removeRow(r);
					}
				}
			}
		});
		
		bOutputFolder.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fc.showSaveDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					taOutputFolder.setText(fc.getSelectedFile().toString());
				}
			}
		});
		
		bCsvOutput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showSaveDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					
					String name = fc.getSelectedFile().toString();
					if(!name.endsWith(".csv")){
						name += ".csv";
					}
			
					LocalContext.get().setRepData(new File(name).getAbsolutePath());
					
					taCsvOutput.setText(name);
				}
			}
		});
		
		exportAscii.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(!exportAscii.isSelected()){
					viewAsciiOutput.setEnabled(false);
				}else{
					viewAsciiOutput.setEnabled(true);
				}
			}
		});
		
		bCsvInput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					inputCsv = fc.getSelectedFile().toString();
					variables.clear();

					getController().importXYCsv((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), inputCsv, variables);
				}
			}
		});
		
		bCsvMap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					mapCsv = fc.getSelectedFile().toString();
					vmap.clear();
					
					getController().importMapCsv((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), mapCsv, vmap);
				}
			}
		});
		
		bSort.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().sortCsv(inputCsv);
				bSort.setEnabled(false);
			}
		});
		
		tLVariables.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					Vector<String> v;
					Set<String> m = new TreeSet<String>();
					int i=0;
					for(int r : tLVariables.getSelectedRows()){
						r = r - (i++);
						v = new Vector<String>();
						v.add((String) tLVariables.getModel().getValueAt(r, 0));
						m.add((String) tLVariables.getModel().getValueAt(r, 0));
						((DefaultTableModel) tCVariables.getModel()).addRow(v);
						((DefaultTableModel) tLVariables.getModel()).removeRow(r);
					}
				}
			}
		});
		
		tLMap.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 1){
					bCsvApply.setEnabled(true);
				}
			}
		});
		
		bCsvApply.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = (String) tLMap.getModel().getValueAt(tLMap.getSelectedRow(),0);
				try{
					CsvReader cr = new CsvReader(mapCsv);
					cr.setDelimiter(';');
					cr.readHeaders();
					
					Map<Integer, Double> map = new HashMap<Integer, Double>();
					while(cr.readRecord()){
						map.put(Integer.parseInt(cr.get("id")), Double.parseDouble(cr.get(name)));
					}
					cr.close();
					
					for(int i=0; i<tChanges.getRowCount(); i++){
						if(map.containsKey((Integer) tChanges.getModel().getValueAt(i, 0))){
							tChanges.getModel().setValueAt(true, i, 1);
							tChanges.getModel().setValueAt(map.get((Integer) tChanges.getModel().getValueAt(i, 0))+"", i, 2);
						}
					}
					
				} catch (IOException | FinalizedException | CatastrophicException e1) {
					e1.printStackTrace();
				}
			}
		});
				
		bmVAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				while(tLVariables.getRowCount() > 0){
					v = new Vector<String>();
					v.add((String) tLVariables.getModel().getValueAt(0, 0));
					((DefaultTableModel) tCVariables.getModel()).addRow(v);
					((DefaultTableModel) tLVariables.getModel()).removeRow(0);
				}
			}
		});
		
		bmVAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				int i=0;
				for(int r : tLVariables.getSelectedRows()){
					r = r - (i++);
					v = new Vector<String>();
					v.add((String) tLVariables.getModel().getValueAt(r, 0));
					((DefaultTableModel) tCVariables.getModel()).addRow(v);
					((DefaultTableModel) tLVariables.getModel()).removeRow(r);
				}
			}
		});
		
		bmVRem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<String> v;
				int i=0;
				for(int r : tCVariables.getSelectedRows()){
					r = r - (i++);
					v = new Vector<String>();
					v.add((String) tCVariables.getModel().getValueAt(r, 0));
					((DefaultTableModel) tLVariables.getModel()).addRow(v);
					((DefaultTableModel) tCVariables.getModel()).removeRow(r);
				}
			}
		});
		
		bHeader.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					initWithAsciiGrid(fc.getSelectedFile().toString());
				}
			}
		});
		
		bShapeInput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					inputShape = fc.getSelectedFile().toString();
					shapes.clear();
					getController().importShapefile((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), inputShape, shapes);
				}
			}
		});
		
		bCorrespondance.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					LocalContext.get().setRepData(fc.getSelectedFile().toString());
					
					taCorrespondance.setText(fc.getSelectedFile().toString());
				}
			}
		});
		
		cbEnvelope.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(cbEnvelope.isSelected()){
					spMinX.setEnabled(true);
					spMaxX.setEnabled(true);
					spMinY.setEnabled(true);
					spMaxY.setEnabled(true);
				}else{
					spMinX.setEnabled(false);
					spMaxX.setEnabled(false);
					spMinY.setEnabled(false);
					spMaxY.setEnabled(false);
				}
			}
		});
		
		bsSFAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				double c = (Double) spCellsize.getValue();
				spCellsize.setValue(c+1);
				for(int i=0; i<tCellsize.getRowCount(); i++){
					if(c == (Double) tCellsize.getValueAt(i, 0)){
						return;
					}
				}
				Vector<Double> v = new Vector<Double>();
				v.add(c);
				((DefaultTableModel) tCellsize.getModel()).addRow(v);
			}
		});
		
		bsSFRem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int i=0;
				for(int r : tCellsize.getSelectedRows()){
					r = r - (i++);
					((DefaultTableModel) tCellsize.getModel()).removeRow(r);
				}
			}
		});
		
		bNewClassification.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = ((DefaultTableModel) tClassification.getModel()).getRowCount();
				Object[] obj = new Object[2];
				obj[0] = "";
				obj[1] = row+1+"";
				((DefaultTableModel) tClassification.getModel()).addRow(obj);
			}
		});
		
		bRemoveClassification.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = ((DefaultTableModel) tClassification.getModel()).getRowCount();
				if(row > 0){
					((DefaultTableModel) tClassification.getModel()).removeRow(row-1);
				}
			}
		});
		
		bsAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int s = (Integer) spSize.getValue();
				if(s%2 == 0){
					s++;
				}
				spSize.setValue(s+2);
				for(int i=0; i<tSize.getRowCount(); i++){
					if(s == (Integer) tSize.getValueAt(i, 0)){
						return;
					}
				}
				Vector<Integer> v = new Vector<Integer>();
				v.add(s);
				((DefaultTableModel) tSize.getModel()).addRow(v);
			}
		});
	
		bImportEnvelope.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					//System.out.println(fc.getSelectedFile().toString());
					String propertiesFile = fc.getSelectedFile().toString();
					try{
						Properties properties = new Properties();
						FileInputStream in = new FileInputStream(propertiesFile);
						properties.load(in);
						in.close();
							
						if(properties.containsKey("minx")){
							spMinX.setValue(new Double(properties.getProperty("minx")));
							spMaxX.setValue(new Double(properties.getProperty("maxx")));
							spMinY.setValue(new Double(properties.getProperty("miny")));
							spMaxY.setValue(new Double(properties.getProperty("maxy")));
						}else if(properties.containsKey("minX")){
							spMinX.setValue(new Double(properties.getProperty("minX")));
							spMaxX.setValue(new Double(properties.getProperty("maxX")));
							spMinY.setValue(new Double(properties.getProperty("minY")));
							spMaxY.setValue(new Double(properties.getProperty("maxY")));
						}
								
					}catch(FileNotFoundException ex){
						ex.printStackTrace();
					}catch(IOException ex){
						ex.printStackTrace();
					}
					
					cbEnvelope.setSelected(true);
				}
			}
		});
		
		bExportEnvelope.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(LocalContext.get().getRepData());
				if(fc.showOpenDialog(TreatmentPanel.wizard.getCurrent()) == JFileChooser.APPROVE_OPTION){
					//System.out.println(fc.getSelectedFile().toString());
					String propertiesFile = fc.getSelectedFile().toString();
					if(!propertiesFile.endsWith(".properties")){
						propertiesFile += ".properties";
					}
					try{
						Properties properties = new Properties();
						properties.setProperty("minx", spMinX.getValue().toString());
						properties.setProperty("maxx", spMaxX.getValue().toString());
						properties.setProperty("miny", spMinY.getValue().toString());
						properties.setProperty("maxy", spMaxY.getValue().toString());
												
						FileOutputStream out = new FileOutputStream(propertiesFile);
						properties.store(out, "");
						out.close();
				
					}catch(FileNotFoundException ex){
						ex.printStackTrace();
					}catch(IOException ex){
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	public void displayAttributes(String inputShape, boolean fromFile, Map<String, String> attributes, double[] envelope){
		taShapeInput.setText(inputShape);
		
		for(; 0<((DefaultTableModel) tAttribute.getModel()).getRowCount();){
			((DefaultTableModel) tAttribute.getModel()).removeRow(0);
		}
		
		Vector<String> vec;
		for(Entry<String, String> a : attributes.entrySet()){
			vec = new Vector<String>();
			vec.add(a.getKey());
			vec.add(a.getValue());
			((DefaultTableModel) tAttribute.getModel()).addRow(vec);
		}
		
		cbEnvelope.setEnabled(true);
		tAttribute.setEnabled(true);
		lCorrespondance.setEnabled(true);
		taCorrespondance.setEnabled(true);
		bCorrespondance.setEnabled(true);
		spCellsize.setEnabled(true);
		tCellsize.setEnabled(true);
		bsSFAdd.setEnabled(true);
		bsSFRem.setEnabled(true);
		taOutputFolder.setEnabled(true);
		bOutputFolder.setEnabled(true);
		viewAsciiOutput.setEnabled(true);
		bImportEnvelope.setEnabled(true);
		bExportEnvelope.setEnabled(true);
		spMinX.setValue(envelope[0]);
		spMaxX.setValue(envelope[1]);
		spMinY.setValue(envelope[2]);
		spMaxY.setValue(envelope[3]);
	}
	
	public void displayVMap(){
		
		for(; 0<((DefaultTableModel) tLMap.getModel()).getRowCount();){
			((DefaultTableModel) tLMap.getModel()).removeRow(0);
		}
		
		Vector<String> vec;
		for(String v : vmap){
			vec = new Vector<String>();
			vec.add(v);
			((DefaultTableModel) tLMap.getModel()).addRow(vec);
		}
	}
	
	public void displayVariables(){
		taCsvInput.setText(inputCsv);
		
		for(; 0<((DefaultTableModel) tLVariables.getModel()).getRowCount();){
			((DefaultTableModel) tLVariables.getModel()).removeRow(0);
		}
		
		for(; 0<((DefaultTableModel) tCVariables.getModel()).getRowCount();){
			((DefaultTableModel) tCVariables.getModel()).removeRow(0);
		}
		
		Vector<String> vec;
		for(String v : variables){
			vec = new Vector<String>();
			vec.add(v);
			((DefaultTableModel) tLVariables.getModel()).addRow(vec);
		}
	}
	
	private static void displayWindowSizeInMeters(double d) {
		lSizeMeters.setText("size = "+d+" meters");
	}
	
	private static void displayMultiScalesMetrics(Set<String> m){
		Vector<String> vec;
		for(String metric : MatrixMetricManager.getStatsMetricNames(m)){
			vec = new Vector<String>();
			vec.add(metric);
			((DefaultTableModel) tLMSMetrics.getModel()).addRow(vec);
		}		
	}
	
	private static void removeMultiScalesMetrics(){
		for(; 0<((DefaultTableModel) tLMSMetrics.getModel()).getRowCount();){
			((DefaultTableModel) tLMSMetrics.getModel()).removeRow(0);
		}
		
		for(; 0<((DefaultTableModel) tCMSMetrics.getModel()).getRowCount();){
			((DefaultTableModel) tCMSMetrics.getModel()).removeRow(0);
		}
	}

	private static void removeMultiScalesMetrics(Set<String> m){
		String cur;
		for(int r=0; r<tLMSMetrics.getRowCount(); r++){
			cur = (String) tLMSMetrics.getModel().getValueAt(r, 0);
			for(String metric : m){
				if(cur.endsWith(metric)){
					((DefaultTableModel) tLMSMetrics.getModel()).removeRow(r);
					r--;
				}
			}
		}
		for(int r=0; r<tCMSMetrics.getRowCount(); r++){
			cur = (String) tCMSMetrics.getModel().getValueAt(r, 0);
			for(String metric : m){
				if(cur.endsWith(metric)){
					((DefaultTableModel) tCMSMetrics.getModel()).removeRow(r);
					r--;
				}
			}
		}
	}
	
	private static void initWithAsciiGrid(String ascii) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(ascii));

			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			st.nextToken();
			spncols.setValue(Integer.parseInt(st.nextToken()));

			line = br.readLine();
			st = new StringTokenizer(line, " ");
			st.nextToken();
			spnrows.setValue(Integer.parseInt(st.nextToken()));

			line = br.readLine();
			st = new StringTokenizer(line, " ");
			st.nextToken();
			spxllcorner.setValue(Double.parseDouble(st.nextToken()));

			line = br.readLine();
			st = new StringTokenizer(line, " ");
			st.nextToken();
			spyllcorner.setValue(Double.parseDouble(st.nextToken()));

			line = br.readLine();
			st = new StringTokenizer(line, " ");
			st.nextToken();
			spcellsize.setValue(Double.parseDouble(st.nextToken()));

			line = br.readLine();
			if(line != null && line.equals("")){
				st = new StringTokenizer(line, " ");
				if(st != null && st.hasMoreTokens()){
					if (st.nextToken().equalsIgnoreCase("NODATA_value")) {
						spnodatavalue.setValue(Integer.parseInt(st.nextToken()));
					}
				}
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void locateComponents();
	
	@Override
	public void display() {
		locateComponents();
	}
	
	private static void setEnabledDMax(boolean enabled){
		if(enabled){
			lFriction.setText("dMax = "+(((Integer) spSize.getValue()-1)/2)*inputMatrix.iterator().next().cellsize()+" m     friction file :");
		}else{
			lFriction.setText("friction file :");
		}
	}
	
	protected static void displayDeltaInMeters() {
		double d = (Integer) spDelta.getValue() * inputMatrix.iterator().next().cellsize();
		lDeltaMeters.setText("delta = "+d+" meters");
	}
	
	protected static void displayMetrics(String processType){
		removeMultiScalesMetrics();
		
		for(; 0<((DefaultTableModel) tLMetrics.getModel()).getRowCount();){
			((DefaultTableModel) tLMetrics.getModel()).removeRow(0);
		}
		
		Vector<String> vec;
		Set<Integer> values;
		switch(processType){
		case "values metrics": 
			values = new HashSet<Integer>();
			for(Matrix m : inputMatrix){
				for(double v : m.values()){
					if(v != 0 && v != Raster.getNoDataValue()){
						values.add(new Double(v).intValue());
					}
				}
			}
			for(String metric : MatrixMetricManager.getValueMetricNames(values)){
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			}
			break;
		case "couples metrics": 
			values = new HashSet<Integer>();
			for(Matrix m : inputMatrix){
				for(double v : m.values()){
					if(v != 0 && v != Raster.getNoDataValue()){
						values.add(new Double(v).intValue());
					}
				}
			}
			for(String metric : MatrixMetricManager.getCoupleMetricNames(values)){
				System.out.println(metric);
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			}
			break;
		case "patches metrics": 
			values = new HashSet<Integer>();
			for(Matrix m : inputMatrix){
				for(double v : m.values()){
					if(v != 0 && v != Raster.getNoDataValue()){
						values.add(new Double(v).intValue());
					}
				}
			}
			for(String metric : MatrixMetricManager.getPatchMetricNames(values)){
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			}
			break;
		case "connectivity metrics": 
			values = new HashSet<Integer>();
			for(Matrix m : inputMatrix){
				for(double v : m.values()){
					if(v != 0 && v != Raster.getNoDataValue()){
						values.add(new Double(v).intValue());
					}
				}
			}
			for(String metric : MatrixMetricManager.getConnectivityMetricNames(values)){
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			} 
			break;
		case "diversity metrics": 
			values = new HashSet<Integer>();
			for(Matrix m : inputMatrix){
				for(double v : m.values()){
					if(v != 0 && v != Raster.getNoDataValue()){
						values.add(new Double(v).intValue());
					}
				}
			}
			for(String metric : MatrixMetricManager.getDiversityMetricNames(values)){
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			} 
			break;
		case "landscape grain": 
			values = new HashSet<Integer>();
			for(Matrix m : inputMatrix){
				for(double v : m.values()){
					if(v != 0 && v != Raster.getNoDataValue()){
						values.add(new Double(v).intValue());
					}
				}
			}
			for(String metric : MatrixMetricManager.getGrainMetricNames(values)){
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			} 
			break;
		case "quantitative metrics": 
			for(String metric : MatrixMetricManager.getQuantitativeMetricNames()){
				vec = new Vector<String>();
				vec.add(metric);
				((DefaultTableModel) tLMetrics.getModel()).addRow(vec);
			} 
			break;
		}
	}
	
	@Override
	public boolean hasPrevious() {
		return true;
	}
	
	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean validateNext(List<String> list) {
		return false;
	}

	@Override
	public WizardPanel next(Wizard wizard) {
		return null;
	}
	
	@Override
	public boolean canRun() {
		return true;
	}
	
	public void displayIhm(){
		displayMetrics(cbType.getSelectedItem().toString());
		displayDeltaInMeters();
		displayValues();
		taAsciiInput.setText(inputAscii);
		enabledIhm();
		
		//enabledImportation();
	}
	
	public void displayIhm2(String ascii){
		
		Vector<String> v = new Vector<String>();
		v.add(ascii);
		((DefaultTableModel) tMatrix.getModel()).addRow(v);
		
		tMatrix.setEnabled(true);
		taOutputFolder.setEnabled(true);
		bOutputFolder.setEnabled(true);
		viewAsciiOutput.setEnabled(true);
		
		//enabledImportation();
	}
	
	public void displayIhm3(String ascii){
		
		Vector<String> v = new Vector<String>();
		v.add(ascii);
		v.add("1.0");
		((DefaultTableModel) tFuzion.getModel()).addRow(v);
		
		tFuzion.setEnabled(true);
		taOutputFolder.setEnabled(true);
		bOutputFolder.setEnabled(true);
		viewAsciiOutput.setEnabled(true);
	}
	
	public void displayIhm4(String ascii){
		
		displayValues2();
		taAsciiFilter.setText(ascii);
		bViewAsciiFilter.setEnabled(true);
	}
	
	public static void displayValues2(){
		for(; 0<tFiltersModel.getRowCount();){
			tFiltersModel.removeRow(0);
		}
		
		Set<Integer> values = new TreeSet<Integer>();
		for(Matrix m : filterMatrix){
			values.addAll(m.values());
		}
		values.add(Raster.getNoDataValue());
		
		Vector<Integer> vec;
		for(Integer v : values){
			vec = new Vector<Integer>();
			vec.add(v);
			tFiltersModel.addRow(vec);
		}
	}
	
	public static void displayValues(){
		
		for(; 0<tCIM.getRowCount();){
			tCIM.removeRow(0);
			tFM.removeRow(0);
			tDistancesModel.removeRow(0);
		}
		
		for(; 0<tCNIM.getRowCount();){
			tCNIM.removeRow(0);
			tNFM.removeRow(0);
		}
		
		Set<Integer> values = new TreeSet<Integer>();
		for(Matrix m : inputMatrix){
			values.addAll(m.values());
		}
		values.add(Raster.getNoDataValue());
		
		Vector<Integer> vec;
		for(Integer v : values){
			vec = new Vector<Integer>();
			vec.add(v);
			tCIM.addRow(vec);
			tFM.addRow(vec);
			tCNIM.addRow(vec);
			tNFM.addRow(vec);
			tDistancesModel.addRow(vec);
		}
		
		spNoData.setValue(Raster.getNoDataValue());
		oldNoData = Raster.getNoDataValue();
		
		values.clear();
		for(Matrix m : inputMatrix){
			for(int v : m.values()){
				if(v != Raster.getNoDataValue()){
					values.add(v);
				}
			}
		}
		values.add(Raster.getNoDataValue());
		
		String[] columnNames = {"value", "search and replace", "new value"};
		Object[][] data = new Object[values.size()][3];
		
		tChanges = new JTable();
		tChanges.setRowHeight(30);
		tChanges.setModel(new DefaultTableModel(data, columnNames){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if(columnIndex == 0){
					return false;
				}
				return true;
			}
		});
		
		tChanges.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));	
		tChanges.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tChanges.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 1L;
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row,
					int column) {

				if(value == null){
					JCheckBox cb = new JCheckBox();
					cb.setSelected(false);
					return cb;
				}
				if (value instanceof Boolean) {
					JCheckBox cb = new JCheckBox();
					cb.setSelected(((Boolean) value).booleanValue());
					return cb;
				}
				if (value instanceof JCheckBox) {
					return (JCheckBox) value;
				}
				return new JTextField(value.toString());
			}
		});
		
		int index=0;
		for(Integer v : values){
			tChanges.getModel().setValueAt(v, index, 0);
			tChanges.getModel().setValueAt(false, index, 1);
			index++;
		}
		pValues.setViewportView(tChanges);
	}
	
	public void enabledIhm() {
		bViewAsciiInput.setEnabled(true);
		cbType.setEnabled(true);
		cbShape.setEnabled(true);
		spSize.setEnabled(true);
		spMaxRate.setEnabled(true);
		bsAdd.setEnabled(true);
		tSize.setEnabled(true);
		bsRem.setEnabled(true);
		spGSize.setEnabled(true);	
		bsgAdd.setEnabled(true);
		tGSize.setEnabled(true);
		bsgRem.setEnabled(true);
		spDelta.setEnabled(true);
		cbF.setEnabled(true);
		cbNF.setEnabled(true);
		rbPixel.setEnabled(true);
		rbPoint.setEnabled(true);
		rbGPixel.setEnabled(true);
		bmAll.setEnabled(true);
		bmAdd.setEnabled(true);
		bmRem.setEnabled(true);
		bmsmAll.setEnabled(true);
		bmsmAdd.setEnabled(true);
		bmsmRem.setEnabled(true);
		taOutputFolder.setEnabled(true);
		bOutputFolder.setEnabled(true);
		viewAsciiOutput.setEnabled(true);
		exportCsv.setEnabled(true);
		exportAscii.setEnabled(true);
		taCsvOutput.setEnabled(true);
		bCsvOutput.setEnabled(true);
		pValues.setEnabled(true);
		spNoData.setEnabled(true);
		bSort.setEnabled(true);
		bmVAll.setEnabled(true);
		bmVAdd.setEnabled(true);
		bmVRem.setEnabled(true);
		spncols.setEnabled(true);
		spnrows.setEnabled(true);
		spxllcorner.setEnabled(true);
		spyllcorner.setEnabled(true);
		spcellsize.setEnabled(true);
		spnodatavalue.setEnabled(true);
		bHeader.setEnabled(true);
		spCellsize.setEnabled(true);
		bsSFAdd.setEnabled(true);
		bsSFRem.setEnabled(true);
		tAttribute.setEnabled(true);
		tCellsize.setEnabled(true);
		tDistances.setEnabled(true);
		tFilters.setEnabled(true);
		bNewClassification.setEnabled(true);
		bRemoveClassification.setEnabled(true);
		tClassification.setEnabled(true);
		rbRook.setEnabled(true);
		rbQueen.setEnabled(true);
		rbEuclidianDistance.setEnabled(true);
		spEuclidianDistanceCluster.setEnabled(true);
		rbFunctionalDistance.setEnabled(true);
		spFunctionalDistanceCluster.setEnabled(true);
		taFrictionCluster.setEnabled(true);
		bFrictionCluster.setEnabled(true);
	}
	
	public static void enabledAfterDispatch(){
		taPixel.setEnabled(false);
		taPoint.setEnabled(false);
		bPixel.setEnabled(false);
		bPoint.setEnabled(false);
		spNPixel.setEnabled(false);
		cbMinDistance.setEnabled(false);
		spMinDistance.setEnabled(false);
		cbCI.setEnabled(false);
		cbCNI.setEnabled(false);
		bRunPixel.setEnabled(false);
		
		bExportPixel.setEnabled(true);
		bExportPoint.setEnabled(true);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////// imports ///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	public void importTreatment(String file) {
		try{
			Properties properties = new Properties();
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			in.close();
			
			doImport(properties);
			
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	//public abstract void importTreatment(String file);
	
	/*
	public void enabledImportation(){
		System.out.println("appel");
		if(importationFile != null){
			try{
				Properties properties = new Properties();
				FileInputStream in = new FileInputStream(importationFile);
				properties.load(in);
				in.close();
				
				doImport(properties);
				
				importationFile = null;
				
			}catch(FileNotFoundException ex){
				ex.printStackTrace();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}
	*/
	
	public abstract void doImport(Properties properties);
	
	public void importInputAscii(Properties properties){
		taAsciiInput.setText("");
		if(properties.containsKey("input_ascii")){
			String prop = properties.getProperty("input_ascii");
			inputAscii = prop;
			inputMatrix.clear();
			getController().importAsciiGrid(this, inputMatrix, prop);
		}
	}
	
	public void importInputCsv(Properties properties){
		taCsvInput.setText("");
		if(properties.containsKey("input_csv")){
			String prop = properties.getProperty("input_csv");
			inputCsv = prop;
			variables.clear();
			getController().importXYCsv(this, prop, variables);
		}
	}
	
	public void importMapCsv(Properties properties){
		taCsvInput.setText("");
		if(properties.containsKey("map_csv")){
			String prop = properties.getProperty("map_csv");
			mapCsv = prop;
			vmap.clear();
			getController().importMapCsv(this, prop, vmap);
		}
	}
	
	public void importInputShapefile(Properties properties){
		taShapeInput.setText("");
		if(properties.containsKey("input_shapefile")){
			String prop = properties.getProperty("input_shapefile");
			inputShape = prop;
			shapes.clear();
			getController().importShapefile(this, prop, shapes);
		}
	}
	
	public void importProcessType(Properties properties){
		if(properties.containsKey("process_type")){
			cbType.setSelectedItem(properties.getProperty("process_type"));
		}
	}
	
	public void importShape(Properties properties){
		if(properties.containsKey("shape")){
			WindowShapeType shape = WindowShapeType.get(properties.getProperty("shape"));
			cbShape.setSelectedItem(shape);
		}
	}
	
	public void importFriction(Properties properties){
		if(properties.containsKey("friction")){
			String prop = properties.getProperty("friction");
			taFriction.setText(prop);
		}
	}
	
	public void importFrictionMatrix(Properties properties){
		if(properties.containsKey("friction_ascii")){
			String prop = properties.getProperty("friction");
			taFriction.setText(prop);
			getController().importAsciiGridFriction(this, frictionMatrix, prop);
		}
	}
	/*
	public void importWindowSize(Properties properties){
		if(properties.containsKey("window_size")){
			spSize.setValue(Integer.parseInt(properties.getProperty("window_size")));
		}
	}*/
	
	public void importWindowSizes(Properties properties){
		for(; 0<tSize.getRowCount();){
			((DefaultTableModel) tSize.getModel()).removeRow(0);
		}
		if(properties.containsKey("window_sizes")){
			String prop = properties.getProperty("window_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			Vector<Integer> vi;
			for(String w : ws){
				vi = new Vector<Integer>();
				vi.add(Integer.parseInt(w));
				((DefaultTableModel) tSize.getModel()).addRow(vi);
			}
		}
	}
	
	public void importCellSizes(Properties properties){
		for(; 0<tCellsize.getRowCount();){
			((DefaultTableModel) tCellsize.getModel()).removeRow(0);
		}
		if(properties.containsKey("cell_sizes")){
			String prop = properties.getProperty("cell_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			Vector<Double> vi;
			for(String w : ws){
				vi = new Vector<Double>();
				vi.add(Double.parseDouble(w));
				((DefaultTableModel) tCellsize.getModel()).addRow(vi);
			}
		}
	}
	
	public void importEnvelope(Properties properties){
		if(properties.containsKey("minx")){
			cbEnvelope.setSelected(true);
			spMinX.setValue(Double.parseDouble(properties.getProperty("minx")));
			spMaxX.setValue(Double.parseDouble(properties.getProperty("maxx")));
			spMinY.setValue(Double.parseDouble(properties.getProperty("miny")));
			spMaxY.setValue(Double.parseDouble(properties.getProperty("maxy")));
		}else{
			cbEnvelope.setSelected(false);
		}	
	}
	
	/*
	public void importGridSize(Properties properties){
		if(properties.containsKey("grid_size")){
			spGSize.setValue(Integer.parseInt(properties.getProperty("grid_size")));
		}
	}*/
	
	public void importGridSizes(Properties properties){
		for(; 0<tGSize.getRowCount();){
			((DefaultTableModel) tGSize.getModel()).removeRow(0);
		}
		if(properties.containsKey("grid_sizes")){
			String prop = properties.getProperty("grid_sizes");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ws = prop.split(";");
			Vector<Integer> vi;
			for(String w : ws){
				vi = new Vector<Integer>();
				vi.add(new Integer(w));
				((DefaultTableModel) tGSize.getModel()).addRow(vi);
			}
		}
	}

	public void importMaximumNoValueRate(Properties properties){
		if(properties.containsKey("maximum_nodata_value_rate")){
			spMaxRate.setValue(Integer.parseInt(properties.getProperty("maximum_nodata_value_rate")));
		}
	}

	public void importDeltaDisplacement(Properties properties){
		if(properties.containsKey("delta_displacement")){
			int d = Integer.parseInt(properties.getProperty("delta_displacement"));
			spDelta.setValue(d);
		}
	}
	
	public void importInterpolation(Properties properties){
		if(properties.containsKey("interpolation")){
			cbInterpolate.setSelected(Boolean.parseBoolean(properties.getProperty("interpolation")));
		}
	}
	
	public void importPixels(Properties properties){
		rbPixel.setSelected(false);
		if(properties.containsKey("pixels")){
			rbPixel.setSelected(true);
			taPixel.setText(properties.getProperty("pixels"));
			pixels = CoordinateManager.initWithPixels(taPixel.getText());
		}
	}
	
	public void importPoints(Properties properties){
		rbPoint.setSelected(false);
		if(properties.containsKey("points")){
			rbPoint.setSelected(true);
			taPoint.setText(properties.getProperty("points"));
			pixels = CoordinateManager.initWithPoints(inputMatrix.iterator().next(), taPoint.getText());
		}
	}
	
	public void importGeneratedPixels(Properties properties){
		rbGPixel.setSelected(false);
		if(properties.containsKey("number_generated_pixels")){
			rbGPixel.setSelected(true);
			spNPixel.setValue(new Integer(properties.getProperty("number_generated_pixels")));
			
			importMinimumDistance(properties);
			importFilters(properties);
			importUnfilters(properties);
			
			int distance = -1;
			if(cbMinDistance.isSelected()){
				distance = (Integer) spMinDistance.getValue();
			}
			
			Set<Integer> with = null;
			if(cbCI.isSelected()){
				with = new HashSet<Integer>();
				for(int r : tCI.getSelectedRows()){
					with.add((Integer) tCI.getModel().getValueAt(r, 0));
				}
			}
			
			Set<Integer> without = null;
			if(cbCNI.isSelected()){
				without = new HashSet<Integer>();
				for(int r : tCNI.getSelectedRows()){
					without.add((Integer) tCNI.getModel().getValueAt(r, 0));
				}
			}
			
			try{				
				getController().generatePixels(pixels, inputMatrix.iterator().next(), (Integer) spNPixel.getValue(), distance, with, without);
			}catch(IllegalArgumentException ex){
				ex.printStackTrace();
			}
		}
	}
	
	public void importMinimumDistance(Properties properties){
		cbMinDistance.setSelected(false);
		if(properties.containsKey("minimum_distance")){
			cbMinDistance.setSelected(true);
			spMinDistance.setValue(new Integer(properties.getProperty("minimum_distance")));
		}
	}
	
	public void importLookupTable(Properties properties){
		taCorrespondance.setText("");
		if(properties.containsKey("lookup_table")){
			taCorrespondance.setText(properties.getProperty("lookup_table"));
		}
	}
	
	public void importAttribute(Properties properties){
		if(properties.containsKey("attribute")){
			String prop = properties.getProperty("attribute");
			for(int i=0; i<tAttribute.getModel().getRowCount(); i++){
				if(tAttribute.getValueAt(i, 0).equals(prop)){
					tAttribute.getSelectionModel().addSelectionInterval(i, i);
				}	
			}
		}
	}

	public void importFilters(Properties properties){
		cbF.setSelected(false);
		for(int r : tF.getSelectedRows()){
			tF.getSelectionModel().removeSelectionInterval(r, r);
		}
		if(properties.containsKey("filters")){	
			String prop = properties.getProperty("filters");
			cbF.setSelected(true);
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			Integer fi;
			for(String f : fs){
				fi = new Integer(f);
				for(int i=0; i<tF.getModel().getRowCount(); i++){
					if(tF.getValueAt(i, 0).equals(fi)){
						tF.getSelectionModel().addSelectionInterval(i, i);
					}	
				}
			}
		}
	}

	public void importUnfilters(Properties properties){
		cbNF.setSelected(false);
		for(int r : tNF.getSelectedRows()){
			tNF.getSelectionModel().removeSelectionInterval(r, r);
		}
		if(properties.containsKey("unfilters")){
			String prop = properties.getProperty("unfilters");
			cbNF.setSelected(true);
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] fs = prop.split(";");
			Integer fi;
			for(String f : fs){
				fi = new Integer(f);
				for(int i=0; i<tNF.getModel().getRowCount(); i++){
					if(tNF.getValueAt(i, 0).equals(fi)){
						tNF.getSelectionModel().addSelectionInterval(i, i);
					}	
				}
			}
		}
	}
	
	public void importMetrics(Properties properties){
		if(properties.containsKey("metrics")){
			String prop = properties.getProperty("metrics");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			Vector<String> vs;
			for(String m : ms){
				vs = new Vector<String>();
				vs.add(m);
				((DefaultTableModel) tCMetrics.getModel()).addRow(vs);
				for(int r=0; r<tLMetrics.getModel().getRowCount(); r++){
					if(((String) tLMetrics.getModel().getValueAt(r, 0)).equalsIgnoreCase(m)){
						((DefaultTableModel) tLMetrics.getModel()).removeRow(r);
						break;
					}
				}
			}
		}
	}
	
	public void importVariables(Properties properties){
		if(properties.containsKey("variables")){
			String prop = properties.getProperty("variables");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			Vector<String> vs;
			for(String m : ms){
				vs = new Vector<String>();
				vs.add(m);
				((DefaultTableModel) tCVariables.getModel()).addRow(vs);
				for(int r=0; r<tLVariables.getModel().getRowCount(); r++){
					if(((String) tLVariables.getModel().getValueAt(r, 0)).equalsIgnoreCase(m)){
						((DefaultTableModel) tLVariables.getModel()).removeRow(r);
						break;
					}
				}
			}
		}
	}
	
	public void importMultiScalesMetrics(Properties properties){
		if(properties.containsKey("multi_scales_metrics")){
			String prop = properties.getProperty("multi_scales_metrics");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			Vector<String> vs;
			for(String m : ms){
				vs = new Vector<String>();
				vs.add(m);
				((DefaultTableModel) tCMSMetrics.getModel()).addRow(vs);
				for(int r=0; r<tLMSMetrics.getModel().getRowCount(); r++){
					if(((String) tLMSMetrics.getModel().getValueAt(r, 0)).equalsIgnoreCase(m)){
						((DefaultTableModel) tLMSMetrics.getModel()).removeRow(r);
						break;
					}
				}
			}
		}
	}
	
	public void importOverlayingMatrix(Properties properties){
		if(properties.containsKey("overlaying_matrix")){
			String prop = properties.getProperty("overlaying_matrix");
			prop = prop.replace("{", "").replace("}", "").replace(" ", "");
			String[] ms = prop.split(";");
			for(String m : ms){
				getController().importAsciiGrid2((TreatmentPanel) TreatmentPanel.wizard.getCurrent(), inputMatrix2, m);
			}
		}
	}
	
	public void importHeader(Properties properties){
		if(properties.containsKey("ncols")){
			spncols.setValue(Integer.parseInt(properties.getProperty("ncols")));
		}
		if(properties.containsKey("nrows")){
			spnrows.setValue(Integer.parseInt(properties.getProperty("nrows")));
		}
		if(properties.containsKey("xllcorner")){
			spxllcorner.setValue(Double.parseDouble(properties.getProperty("xllcorner")));
		}
		if(properties.containsKey("yllcorner")){
			spyllcorner.setValue(Double.parseDouble(properties.getProperty("yllcorner")));
		}
		if(properties.containsKey("cellsize")){
			spcellsize.setValue(Double.parseDouble(properties.getProperty("cellsize")));
		}
		if(properties.containsKey("nodata_value")){
			spnodatavalue.setValue(Integer.parseInt(properties.getProperty("nodata_value")));
		}
	}
	
	public void importOutputFolder(Properties properties){
		taOutputFolder.setText("");
		if(properties.containsKey("output_folder")){
			taOutputFolder.setText(properties.getProperty("output_folder"));
		}
	}
	
	public void importCsvOutput(Properties properties){
		taCsvOutput.setText("");
		if(properties.containsKey("output_csv")){
			taCsvOutput.setText(properties.getProperty("output_csv"));
		}
	}

	public void importExportCsv(Properties properties){
		if(properties.containsKey("export_csv")){
			exportCsv.setSelected(Boolean.parseBoolean(properties.getProperty("export_csv")));
		}
	}

	public void importExportAscii(Properties properties){
		if(properties.containsKey("export_ascii")){
			exportAscii.setSelected(Boolean.parseBoolean(properties.getProperty("export_ascii")));
		}
	}

	public void importVisualizeAscii(Properties properties){
		if(properties.containsKey("visualize_ascii")){
			viewAsciiOutput.setSelected(Boolean.parseBoolean(properties.getProperty("visualize_ascii")));
		}
	}
	
	public void importNoDataValue(Properties properties){
		if(properties.containsKey("nodata_value")){
			spNoData.setValue(Integer.parseInt(properties.getProperty("nodata_value")));
		}
	}
	
	public void importChanges(Properties properties){
		if(properties.containsKey("changes")){
			String prop = properties.getProperty("changes").replace("{", "").replace("}", "");
			String[] cc = prop.split(";");
			String[] vv;
			for(String c : cc){
				c = c.replace("(", "").replace(")", "");
				vv = c.split(",");
				for(int i=0; i<tChanges.getModel().getRowCount(); i++){
					if(Integer.parseInt(vv[0]) == (Integer) tChanges.getModel().getValueAt(i, 0)){
						tChanges.getModel().setValueAt(true, i, 1);
						tChanges.getModel().setValueAt(vv[1], i, 2);
					}
				}
			}
		}
	}
	
	public void importDistances(Properties properties){
		if(properties.containsKey("distance_from")){
			String prop = properties.getProperty("distance_from").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			for(String d : df){
				for(int i=0; i<tDistances.getModel().getRowCount(); i++){
					if(Integer.parseInt(d) == (Integer) tDistances.getModel().getValueAt(i, 0)){
						tDistances.getSelectionModel().addSelectionInterval(i, i);
					}
				}
			}
		}
	}
	
	public void importCluster(Properties properties){
		if(properties.containsKey("cluster")){
			String prop = properties.getProperty("cluster").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			for(String d : df){
				for(int i=0; i<tDistances.getModel().getRowCount(); i++){
					if(Integer.parseInt(d) == (Integer) tDistances.getModel().getValueAt(i, 0)){
						tDistances.getSelectionModel().addSelectionInterval(i, i);
					}
				}
			}
		}
	}
	
	public void importDomains(Properties properties){
		for(; 0<tClassification.getRowCount();){
			((DefaultTableModel) tClassification.getModel()).removeRow(0);
		}
		if(properties.containsKey("domains")){
			String prop = properties.getProperty("domains").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			Vector<String> vec;
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split("-");
				vec = new Vector<String>();
				vec.add(dd[0]);
				vec.add(dd[1]);
				((DefaultTableModel) tClassification.getModel()).addRow(vec);
			}
		}
	}
	
	public void importFactors(Properties properties){
		inputMatrix3.clear();
		for(; 0<tFuzion.getRowCount();){
			((DefaultTableModel) tFuzion.getModel()).removeRow(0);
		}
		if(properties.containsKey("factors")){
			String prop = properties.getProperty("factors").replace("{", "").replace("}", "");
			String[] ds = prop.split(";");
			int r = 0;
			for(String d : ds){
				d = d.replace("(", "").replace(")", "");
				String[] dd = d.split(",");
				getController().importAsciiGrid3(this, inputMatrix3, dd[0]);
				((DefaultTableModel) tFuzion.getModel()).setValueAt(dd[1], r, 1);
				r++;
			}
		}
	}
	
	public void importClusterType(Properties properties){
		if(properties.containsKey("cluster_type")){
			String prop = properties.getProperty("cluster_type");
			if(prop.equalsIgnoreCase("rook")){
				rbRook.setSelected(true);
			}else if(prop.equalsIgnoreCase("queen")){
				rbQueen.setSelected(true);
			}else if(prop.equalsIgnoreCase("euclidian")){
				rbEuclidianDistance.setSelected(true);
			}else if(prop.equalsIgnoreCase("functional")){
				rbFunctionalDistance.setSelected(true);
			}
		}
	}
	
	public void importClusterDistance(Properties properties){
		if(properties.containsKey("cluster_distance")){
			String prop = properties.getProperty("cluster_distance");
			spEuclidianDistanceCluster.setValue(Double.parseDouble(prop));
			spFunctionalDistanceCluster.setValue(Double.parseDouble(prop));
		}
	}
	
	public void importClusterFriction(Properties properties){
		if(properties.containsKey("cluster_friction")){
			String prop = properties.getProperty("cluster_friction");
			taFrictionCluster.setText(prop);
		}
	}
	
	public void importClusterFrictionMatrix(Properties properties){
		if(properties.containsKey("cluster_friction_ascii")){
			String prop = properties.getProperty("cluster_friction_ascii");
			taFrictionCluster.setText(prop);
			getController().importAsciiGridFriction(this, clusterFrictionMatrix, prop);
		}
	}
	
	public void importFilterAscii(Properties properties){
		filterMatrix.clear();
		if(properties.containsKey("ascii_filter")){
			String prop = properties.getProperty("ascii_filter");
			getController().importAsciiGridFilter(this, filterMatrix, prop);
		}
	}
	
	public void importFilterValues(Properties properties){
		if(properties.containsKey("filter_values")){
			String prop = properties.getProperty("filter_values").replace("{", "").replace("}", "");
			String[] df = prop.split(";");
			for(String d : df){
				for(int i=0; i<tFilters.getModel().getRowCount(); i++){
					if(Integer.parseInt(d) == (Integer) tFilters.getModel().getValueAt(i, 0)){
						tFilters.getSelectionModel().addSelectionInterval(i, i);
					}
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////// exports ///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	public void exportTreatment(String file) {
		try{
			Properties properties = new Properties();
			
			properties.setProperty("treatment", this.toString());
			
			doExport(properties);
			
			FileOutputStream out = new FileOutputStream(file);
			properties.store(out,"parameter file generated with Chloe");
			out.close();
		}catch(FileNotFoundException ex){
			ex.printStackTrace();
		}catch(IOException ex){
			ex.printStackTrace();
		} 
	}
	
	public abstract void doExport(Properties properties);

	public void exportInputAscii(Properties properties){
		properties.setProperty("input_ascii", taAsciiInput.getText());
	}
	
	public void exportInputCsv(Properties properties){
		properties.setProperty("input_csv", taCsvInput.getText());
	}
	
	public void exportMapCsv(Properties properties){
		if(mapCsv != null && !mapCsv.equalsIgnoreCase("")){
			properties.setProperty("map_csv", mapCsv);
		}
	}
	
	public void exportInputShapefile(Properties properties){
		properties.setProperty("input_shapefile", taShapeInput.getText());
	}
	
	public void exportLookupTable(Properties properties){
		if(!taCorrespondance.getText().equalsIgnoreCase("")){
			properties.setProperty("lookup_table", taCorrespondance.getText());
		}
	}
	
	public void exportAttribute(Properties properties){
		if(tAttribute.getSelectedRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append(tAttribute.getModel().getValueAt(tAttribute.getSelectedRows()[0], 0));
		
			properties.setProperty("attribute", sb.toString());
		}
	}
	
	public void exportCellSizes(Properties properties){
		if(tCellsize.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append(((Double)tCellsize.getModel().getValueAt(0, 0)).toString());
			for(int r=1; r<tCellsize.getModel().getRowCount(); r++){
				sb.append(';'+((Double) tCellsize.getModel().getValueAt(r, 0)).toString());
			}
			sb.append("}");
			
			properties.setProperty("cell_sizes", sb.toString());
		}
	}
	
	public void exportEnvelope(Properties properties){
		if(cbEnvelope.isSelected()){
			properties.setProperty("minx", spMinX.getValue()+"");
			properties.setProperty("maxx", spMaxX.getValue()+"");
			properties.setProperty("miny", spMinY.getValue()+"");
			properties.setProperty("maxy", spMaxY.getValue()+"");
		}
	}

	public void exportProcessType(Properties properties){
		properties.setProperty("process_type", cbType.getSelectedItem().toString());
	}

	public void exportShape(Properties properties){
		WindowShapeType shape = (WindowShapeType) cbShape.getSelectedItem();
		properties.setProperty("shape", shape.toString());
	}
	
	public void exportFriction(Properties properties){
		WindowShapeType shape = (WindowShapeType) cbShape.getSelectedItem();
		if(shape.equals(WindowShapeType.FUNCTIONAL) && frictionMatrix == null){
			properties.setProperty("friction", taFriction.getText());
		}
	}
	
	public void exportFrictionMatrix(Properties properties){
		WindowShapeType shape = (WindowShapeType) cbShape.getSelectedItem();
		if(shape.equals(WindowShapeType.FUNCTIONAL) && frictionMatrix != null){
			properties.setProperty("friction_ascii", taFriction.getText());
		}
	}
	
	public void exportWindowSize(Properties properties){
		properties.setProperty("window_size", spSize.getValue().toString());
	}
	
	public void exportWindowSizes(Properties properties){
		if(tSize.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append(((Integer)tSize.getModel().getValueAt(0, 0)).toString());
			for(int r=1; r<tSize.getModel().getRowCount(); r++){
				sb.append(';'+((Integer) tSize.getModel().getValueAt(r, 0)).toString());
			}
			sb.append("}");
			
			properties.setProperty("window_sizes", sb.toString());
		}
	}
	
	/*
	public void exportGridSize(Properties properties){
		properties.setProperty("grid_size", spGSize.getValue().toString());
	}
	*/
	
	public void exportGridSizes(Properties properties){
		if(tGSize.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append(((Integer)tGSize.getModel().getValueAt(0, 0)).toString());
			for(int r=1; r<tGSize.getModel().getRowCount(); r++){
				sb.append(';'+((Integer) tGSize.getModel().getValueAt(r, 0)).toString());
			}
			sb.append("}");
			
			properties.setProperty("grid_sizes", sb.toString());
		}
	}
	
	public void exportMaximumNoValueRate(Properties properties){
		properties.setProperty("maximum_nodata_value_rate", spMaxRate.getValue().toString());
	}

	public void exportDeltaDisplacement(Properties properties){
		properties.setProperty("delta_displacement", spDelta.getValue().toString());
	}
	
	public void exportInterpolation(Properties properties){
		if(((Integer) spDelta.getValue()) > 1){
			properties.setProperty("interpolation", Boolean.toString(cbInterpolate.isSelected()));
		}
	}
	
	public void exportPixels(Properties properties){
		if(rbPixel.isSelected()){
			properties.setProperty("pixels", taPixel.getText());
		}
	}
	
	public void exportPoints(Properties properties){
		if(rbPoint.isSelected()){
			properties.setProperty("points", taPoint.getText());
		}
	}
	
	public void exportGeneratedPixels(Properties properties){
		if(rbGPixel.isSelected()){
			properties.setProperty("number_generated_pixels", spNPixel.getValue().toString());
		}
	}
	
	public void exportMinimumDistance(Properties properties){
		if(cbMinDistance.isSelected()){
			properties.setProperty("minimum_distance", spMinDistance.getValue().toString());
		}
	}

	public void exportFilters(Properties properties){
		if(cbF.isSelected() && tF.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			boolean first = true;
			for(int r : tF.getSelectedRows()){
				if(first){
					sb.append(((Integer) tF.getModel().getValueAt(r, 0)).toString());
					first = false;
				}else{
					sb.append(';'+((Integer) tF.getModel().getValueAt(r, 0)).toString());
				}
			}
			sb.append("}");
			
			properties.setProperty("filters", sb.toString());
		}
	}

	public void exportUnfilters(Properties properties){
		if(cbNF.isSelected() && tNF.getSelectedRows().length > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			boolean first = true;
			for(int r : tNF.getSelectedRows()){
				if(first){
					sb.append(((Integer) tNF.getModel().getValueAt(r, 0)).toString());
					first = false;
				}else{
					sb.append(';'+((Integer) tNF.getModel().getValueAt(r, 0)).toString());
				}
			}
			sb.append("}");
			
			properties.setProperty("unfilters", sb.toString());
		}
	}
	
	public void exportMetrics(Properties properties){
		if(tCMetrics.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append((String) tCMetrics.getModel().getValueAt(0, 0));
			for(int r=1; r<tCMetrics.getModel().getRowCount(); r++){
				sb.append(';'+(String) tCMetrics.getModel().getValueAt(r, 0));
			}
			sb.append("}");
			
			properties.setProperty("metrics", sb.toString());
		}
	}
	
	public void exportHeader(Properties properties){
		properties.setProperty("ncols", spncols.getValue()+"");
		properties.setProperty("nrows", spnrows.getValue()+"");
		properties.setProperty("xllcorner", spxllcorner.getValue()+"");
		properties.setProperty("yllcorner", spyllcorner.getValue()+"");
		properties.setProperty("cellsize", spcellsize.getValue()+"");
		properties.setProperty("nodata_value", spnodatavalue.getValue()+"");
	}
	
	public void exportVariables(Properties properties){
		if(tCVariables.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append((String) tCVariables.getModel().getValueAt(0, 0));
			for(int r=1; r<tCVariables.getModel().getRowCount(); r++){
				sb.append(';'+(String) tCVariables.getModel().getValueAt(r, 0));
			}
			sb.append("}");
			
			properties.setProperty("variables", sb.toString());
		}
	}
	
	public void exportMultiScalesMetrics(Properties properties){
		if(tCMSMetrics.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append((String) tCMSMetrics.getModel().getValueAt(0, 0));
			for(int r=1; r<tCMSMetrics.getModel().getRowCount(); r++){
				sb.append(';'+(String) tCMSMetrics.getModel().getValueAt(r, 0));
			}
			sb.append("}");
			
			properties.setProperty("multi_scales_metrics", sb.toString());
		}
	}
	
	public void exportOverlayingMatrix(Properties properties){
		if(tMatrix.getModel().getRowCount() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append((String) tMatrix.getModel().getValueAt(0, 0));
			for(int r=1; r<tMatrix.getModel().getRowCount(); r++){
				sb.append(';'+(String) tMatrix.getModel().getValueAt(r, 0));
			}
			sb.append("}");
			
			properties.setProperty("overlaying_matrix", sb.toString());
		}
	}
	
	public void exportOutputFolder(Properties properties){
		if(!taOutputFolder.getText().equalsIgnoreCase("")){
			properties.setProperty("output_folder", taOutputFolder.getText());
		}
	}
	
	public void exportCsvOutput(Properties properties){
		if(!taCsvOutput.getText().equalsIgnoreCase("")){
			properties.setProperty("output_csv", taCsvOutput.getText());
		}
	}

	public void exportExportCsv(Properties properties){
		properties.setProperty("export_csv", new Boolean(exportCsv.isSelected()).toString());
	}

	public void exportExportAscii(Properties properties){
		properties.setProperty("export_ascii", new Boolean(exportAscii.isSelected()).toString());
	}

	public void exportVisualizeAscii(Properties properties){
		properties.setProperty("visualize_ascii", new Boolean(viewAsciiOutput.isSelected()).toString());
	}
	
	public void exportNoDataValue(Properties properties){
		properties.setProperty("nodata_value", ((Integer) spNoData.getValue()).toString());
	}
	
	public void exportChanges(Properties properties){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		if(oldNoData != (Integer) spNoData.getValue()){
			sb.append("("+oldNoData+","+spNoData.getValue()+");");
		}
		if(tChanges != null){
			tChanges.getColumnModel().getColumn(2).getCellEditor().stopCellEditing();
			for(int i=0; i<tChanges.getModel().getRowCount(); i++){
				if((Boolean)tChanges.getModel().getValueAt(i, 1)){
					sb.append("("+tChanges.getModel().getValueAt(i, 0)+","+tChanges.getModel().getValueAt(i, 2)+");");
				}
			}
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
			sb.append('}');
			properties.setProperty("changes", sb.toString());
		}
	}
	
	public void exportDistances(Properties properties){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		if(tDistances != null){
			for(int r : tDistances.getSelectedRows()){
				sb.append(tDistances.getModel().getValueAt(r, 0)+";");
			}
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append('}');
		properties.setProperty("distance_from", sb.toString());
	}
	
	public void exportCluster(Properties properties){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		if(tDistances != null){
			for(int r : tDistances.getSelectedRows()){
				sb.append(tDistances.getModel().getValueAt(r, 0)+";");
			}
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append('}');
		properties.setProperty("cluster", sb.toString());
	}
	
	public void exportDomains(Properties properties){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		for(int r=0; r<tClassification.getModel().getRowCount(); r++){
			sb.append('(');
			sb.append((String) tClassification.getModel().getValueAt(r, 0));
			sb.append('-');
			sb.append(tClassification.getModel().getValueAt(r, 1));
			sb.append(')');
			sb.append(';');
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append('}');
		properties.setProperty("domains", sb.toString());
	}
	
	public void exportClusterType(Properties properties){
		if(rbRook.isSelected()){
			properties.setProperty("cluster_type", "rook");
		}else if(rbQueen.isSelected()){
			properties.setProperty("cluster_type", "queen");
		}else if(rbEuclidianDistance.isSelected()){
			properties.setProperty("cluster_type", "euclidian");
		}else if(rbFunctionalDistance.isSelected()){
			properties.setProperty("cluster_type", "functional");
		}
	}
	
	public void exportClusterDistance(Properties properties){
		if(rbEuclidianDistance.isSelected()){
			double d = (double) spEuclidianDistanceCluster.getModel().getValue();
			properties.setProperty("cluster_distance", d+"");
		}else if(rbFunctionalDistance.isSelected()){
			double d = (double) spFunctionalDistanceCluster.getModel().getValue();
			properties.setProperty("cluster_distance", d+"");
		}
	}
	
	public void exportClusterFriction(Properties properties){
		if(rbFunctionalDistance.isSelected() && clusterFrictionMatrix == null){
			String f = taFrictionCluster.getText();
			properties.setProperty("cluster_friction", f);
		}
	}
	
	public void exportClusterFrictionMatrix(Properties properties){
		if(rbFunctionalDistance.isSelected() && clusterFrictionMatrix != null){
			String f = taFrictionCluster.getText();
			properties.setProperty("cluster_friction_ascii", f);
		}
	}
	
	public void exportFactors(Properties properties){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		for(int r=0; r<tFuzion.getModel().getRowCount(); r++){
			sb.append('(');
			sb.append((String) tFuzion.getModel().getValueAt(r, 0));
			sb.append(',');
			sb.append(tFuzion.getModel().getValueAt(r, 1));
			sb.append(')');
			sb.append(';');
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append('}');
		properties.setProperty("factors", sb.toString());
	}
	
	public void exportFilterAscii(Properties properties){
		if(!taAsciiFilter.getText().equalsIgnoreCase("")){
			properties.setProperty("ascii_filter", taAsciiFilter.getText());
		}
	}
	
	public void exportFilterValues(Properties properties){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		if(tFilters != null){
			for(int r : tFilters.getSelectedRows()){
				sb.append(tFilters.getModel().getValueAt(r, 0)+";");
			}
		}
		if(sb.length() > 1){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append('}');
		properties.setProperty("filter_values", sb.toString());
	}
	
	public void cleanDomains() {
		while(tClassification.getRowCount() > 0){
			((DefaultTableModel) tClassification.getModel()).removeRow(0);
		}
	}
	
}
