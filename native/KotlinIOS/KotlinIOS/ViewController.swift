import UIKit
import SharedCode

class ViewController: UIViewController {

    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var stations: UIPickerView!
    @IBOutlet weak var departureStationLabel: UILabel!
    @IBOutlet weak var arrivalStationLabel: UILabel!
    @IBOutlet weak var getTimesButton: UIButton!
    @IBOutlet weak var journeyTable: UITableView!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    private var stationData: [Station] = [];
    private var journeyData: [Journey] = [];
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        
        getTimesButton.setTitle("Get Times", for: .normal)
        departureStationLabel.text = "Departure"
        arrivalStationLabel.text = "Arrival"
        label.text = "TrainBoard"
        
        stations.delegate = self
        stations.dataSource = self
    }
    
    @IBAction func getTimes(_ sender: Any) {
        presenter.onTimesRequested()
    }
    
    
}

extension ViewController :  UIPickerViewDelegate, UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return stationData.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        
        if (component == 0) {
            presenter.setDepartureStation(station: stationData[row])
        } else {
            presenter.setArrivalStation(station: stationData[row])
        }
        
        return stationData[row].name
    }
}

extension ViewController : UITableViewDelegate, UITableViewDataSource {
    func formatDateTime(datetime: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd'T'HH:mm:ss.SSSXXX"
        guard let date: Date = dateFormatter.date(from: datetime) else {
            return datetime
        }
        
        let prettyDateFormatter = DateFormatter()
        prettyDateFormatter.dateFormat = "MMM d, h:mm a"
        return prettyDateFormatter.string(from: date)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return journeyData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "JourneyTableViewCell"
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? JourneyTableViewCell else {
            fatalError("The dequeued cell is not an instance of JourneyTableViewCell")
        }
        let journey = journeyData[indexPath.row]
        
        cell.departureTime.text = "Departs \(formatDateTime(datetime: journey.departureTime))"
        cell.arrivalTime.text = "Arrives \(formatDateTime(datetime: journey.arrivalTime))"
        cell.priceRange.text = "From £\(journey.minPrice / 100) to £\(journey.maxPrice / 100)"
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.onViewJourney(journey: journeyData[indexPath.row])
    }
}

extension ViewController: ApplicationContractView {
    func setStations(stations: [Station]) {
        stationData = stations;
            
        if (!stations.isEmpty) {
            presenter.setDepartureStation(station: stations[0])
            presenter.setArrivalStation(station: stations[0])
        }
        
        self.stations.reloadAllComponents()
    }
    
    func showAlert(message: String) {
        let alert = UIAlertController(title: "Alert", message: message, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "Click", style: UIAlertAction.Style.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func openUrl(url: String) {
        guard let link = URL(string: url) else {
          return //be safe
        }
        
        UIApplication.shared.open(link)
    }
    
    func setJourneys(journeys: [Journey]) {
        journeyData = journeys
        
        journeyTable.reloadData()
    }
    
    func openJourneyView() {
        performSegue(withIdentifier: "showJourneyInfo", sender: nil)
    }
}
